package springboot.hotele;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springboot.hotele.models.Gosc;
import springboot.hotele.models.Pokoj;
import springboot.hotele.models.Rezerwacja;
import springboot.hotele.repository.GoscRepo;
import springboot.hotele.repository.PokojRepo;
import springboot.hotele.repository.RezerwacjaRepo;

@Controller
public class Kontrolery {
 
    @Autowired
    private GoscRepo goscRepo;
    @Autowired
    private PokojRepo pokojRepo; 
    @Autowired
    private RezerwacjaRepo rezerwacjaRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Sesja sesja;

    //all       +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       all
    
    @RequestMapping(value = "/wyswietlPokoj", method=RequestMethod.GET) 
    public String wyswietlPokoje(Model model, Authentication auth){
        model.addAttribute("pokojTab", pokojRepo.findAll());
        if(auth==null){
            return "wyswietlPokoj";
        }else if(auth.getAuthorities().toString().equals("[PRACOWNIK]")){           //sprawdzmy czy user ma role PRACOWNIK 
            return "wyswietlPokojPracownik";                                        //jeśli ma to wyświetlamy inną stronę niż wszystkim
        }else{
            return "wyswietlPokoj";
        }
    }

    @RequestMapping(value="/register", method=RequestMethod.GET)                    //wywołanie strony rejestracji
    public String dodajGosc(Model model){
        model.addAttribute("GoscIn", new Gosc());
        return("nowyGosc");
    }

    @RequestMapping(value="/register", method=RequestMethod.POST)                   
    public String dodajGosc(Model model, Gosc gosc){
        if(goscRepo.findByEmail(gosc.getEmail())!=null){                            //sprawdzmy czy dany email jest w bazie
            return "redirect:/register?userexist";                                  //jeśli jest wysyłamy komunikat że taki email już był wprowadzony
        }
        try{
            gosc.setDokument(gosc.getDokument().replaceAll("\\s+",""));             //w dokumencie usuwamy wszystkie spacje
            gosc.setPassword(passwordEncoder.encode(gosc.getPassword()));           //hashujemy hasło
            gosc.setRole("GOSC");                                             //przy rejestracji karzdemu użytkownikowi nadajemy uprawnienia GOSC
            goscRepo.save(gosc);                                                    //dodajemy go do bazy
            return("redirect:/login?registered");                                   //zwracamy stronę logowania z komunikatem o pozytywnym wyniku rejstracji
        }catch(Exception e){
            System.out.println(e.getMessage());
            return "redirect:/register?error";                                      //w razie nie powodzenia rejestracji pokarze się komunkat na stronie rejestracji
        }
    }

    @RequestMapping(value="/wybranyTermin", method=RequestMethod.POST)   
    public String testdaty(Model model,Date start, Date end, Authentication auth,HttpSession session){
        sesja.setStart(start);                              //zapisujemy do pamięci sesji datę
        sesja.setEnd(end);
        Long startLong = start.getTime();                   //zamieniany date z normalnej do takiej zapisanej w LONGu
        Long endLong = end.getTime();
        List<Date> listaDat1= new ArrayList<>();            
        List<Integer> listaZajete= new ArrayList<>();
        for(Long i=startLong;i<=endLong;i+=86400000){       //tworzymy liste dat dzięki której będziemy sprawdzali dostępność pokoju
            listaDat1.add(new Date(i));
        }
        List<Date> listaDat2= new ArrayList<>(listaDat1);   //robimy kopie listy
        listaDat1.remove(listaDat1.size()-1);               //usuwamy ostatnią date bo możemy wynająć pokój gdy inny kończy wynajem
        listaDat2.remove(0);                                //usuwamy pierwszą date bo możemy wynająć pokój gdy inny kończy wynajem
        List<Pokoj> zajete= pokojRepo.findDistinctAllPokojByRezerwacjaDataStartInAndRezerwacjaDataEndIn(listaDat1, listaDat2);
        listaZajete.add(0);
        for(Pokoj p:zajete){                
            listaZajete.add(p.getId());     //z listy zajętych pobieramy ich id 
        }
        if(auth == null){
            model.addAttribute("pokojTab",pokojRepo.findAllByIdNotIn(listaZajete));     //wyszukujem pokoje które nie są zajęte
            return "homeAnon";
        }
        else if(auth.getAuthorities().toString().equals("[GOSC]")){
            model.addAttribute("pokojTab", pokojRepo.findAllByIdNotIn(listaZajete));    //wyszukujem pokoje które nie są zajęte
            return "homeGosc";
        }else{model.addAttribute("pokojTab", pokojRepo.findAll()); return "homeAnon";}  
    }

    //gosc          +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       gosc

    @RequestMapping(value = "/gosc/wyswietlMojeRezerwacje", method=RequestMethod.GET)        //rezerwacje zalogowanego goscia
    public String wyswietlWszystkieRezerwacje(Model model, Authentication auth){
        model.addAttribute("rezerwacjaTab", rezerwacjaRepo.findAllByGoscEmail(auth.getName()));     //wyświetlamy wszytkich gości
        return "wyswietlRezerwacja";
    }

    @RequestMapping(value="/gosc/rezerwujId/{id}", method=RequestMethod.GET)
    public String rezerwuj(Model model, Authentication auth, HttpSession session, @PathVariable("id") Integer id){
        try {
            if(sesja.getEnd()==null){
                model.addAttribute("brakdaty", true);
                model.addAttribute("pokojTab", pokojRepo.findAll());                //jeśli nie wybraliśmy daty to wyświetla komumikat i wszystkie pokoje
                return("homeGosc");
            }
            Gosc gosc = goscRepo.findByEmail(auth.getName());                                      
            Pokoj pokoj = pokojRepo.findByIdIs(id);
            System.out.println(pokoj);
            String koszt=(sesja.getEnd().getTime()-sesja.getStart().getTime())/86400000*pokoj.getCena()+"0zł";      // wyliczamy koszt wynajmu pokoju
            Rezerwacja rezerwacja =  new Rezerwacja(sesja.getStart(),sesja.getEnd(),pokoj,gosc);                    // tworzymy nową rezerwacje
            model.addAttribute("rezerwacjaIn", rezerwacja);
            model.addAttribute("koszt", koszt);
            return("rezerwujId");
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value="/gosc/rezerwujId", method=RequestMethod.POST)
    public String rezerwuj(Model model, Rezerwacja rezerwacja){
        try {   
            rezerwacjaRepo.save(rezerwacja);                                                    //zapisujemy wcześniej utworzoną rezerwacje
            model.addAttribute("zarezerwowano", true);
            model.addAttribute("pokojTab", pokojRepo.findAll());
            return("homeGosc");
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value="/gosc/rezerwuj", method=RequestMethod.GET)
    public String rezerwuj(Model model, Authentication auth){
        Gosc gosc = goscRepo.findByEmail(auth.getName());
        Rezerwacja rezerwacja = new Rezerwacja();
        rezerwacja.setGosc(gosc);
        model.addAttribute("goscInfo", gosc.getImie()+" "+gosc.getNazwisko()+" "+gosc.getEmail());
        model.addAttribute("rezerwacjaIn", rezerwacja);
        model.addAttribute("pokojList", pokojRepo.findAll());
        return("rezerwuj");
    }

    @RequestMapping(value="/gosc/rezerwuj", method=RequestMethod.POST)
    public String rezerwuj(Model model, Rezerwacja rezerwacja, Authentication auth){                    
        try {
            rezerwacja.setGosc(goscRepo.findByEmail(auth.getName()));
            rezerwacjaRepo.save(rezerwacja);
            return("redirect:/gosc/rezerwuj?success");
        } catch (Exception e) {
            return("redirect:/gosc/rezerwuj?error");
        }
    }

    @RequestMapping(value="/gosc/edytujDane", method=RequestMethod.GET)
    public String edytujDane(Model model, Authentication auth){
        model.addAttribute("GoscIn", goscRepo.findByEmail(auth.getName()));     
        return "edytujDaneGosc";
    }
    @RequestMapping(value="/gosc/edytujDane", method=RequestMethod.POST)
    public String edytujDane(Model model, Authentication auth, Gosc nowyGosc){      //edytujemy dane gościa
        try {                                                                                          
            Gosc staryGosc = goscRepo.findByEmail(auth.getName());                  //szukamy gościa
            nowyGosc.setId(staryGosc.getId());                                      //przypisujemy id starego go nowego gościa
            nowyGosc.setRole(staryGosc.getRole());                                  //ustawimy taką samą rolę jak miał stary gość
            nowyGosc.setPassword(staryGosc.getPassword());                          //przypisujemy to samo hasło
            goscRepo.save(nowyGosc);                                                //zapisujemy zmiany do bazy
            return "redirect:/gosc/edytujDane?success";                             //jeśli wszystko się udało to otzrymujemy stosowny komunikat
        } catch (Exception e) {
            return "redirect:/gosc/edytujDane?error";                               //jeśli coś sie wywaliło też dostajemy komunikat
        }
        
    }

    //pracownik     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       pracownik

    @RequestMapping(value="/pracownik/dodajPokoj", method=RequestMethod.GET)        // wywołujemy stronę dodawania pokoju
    public String dodajPokoj(Model model){
        model.addAttribute("PokojIn", new Pokoj());
        return("nowyPokoj");
    }

    @RequestMapping(value="/pracownik/dodajPokoj", method=RequestMethod.POST)     
    public String dodajPokoj(Model model, Pokoj pokoj){
        pokojRepo.save(pokoj);                                                     //zapisujemy pokuj
        return("redirect:/pracownik/dodajPokoj");
    }
    
    @RequestMapping(value = "/pracownik/wyswietlGosc", method=RequestMethod.GET)        //wyświetlamy wszystkich gości
    public String wyswietlGosc(Model model){
        model.addAttribute("goscTab", goscRepo.findAll());
        return "wyswietlGosc";
    }

    @RequestMapping(value = "/pracownik/edytujGosc/{id}", method=RequestMethod.GET)    
    public String edytujGosc(Model model, @PathVariable("id") Integer id){
        model.addAttribute("GoscIn", goscRepo.findById(id));
        return "edytujGosc";
    }

    @RequestMapping(value = "/pracownik/edytujGosc", method=RequestMethod.POST)         //praktycznie to samo co przy edycji gościa
    public String edytujGosc(Model model, Gosc nowyGosc){
        try {
            Gosc staryGosc = goscRepo.findByIdIs(nowyGosc.getId());
            nowyGosc.setRole(staryGosc.getRole());
            nowyGosc.setPassword(staryGosc.getPassword());
            goscRepo.save(nowyGosc);
            return "redirect:/pracownik/edytujGosc/"+nowyGosc.getId()+"?success";
        } catch (Exception e) {
            return "redirect:/pracownik/edytujGosc/"+nowyGosc.getId()+"?error";
        }
    }

    @RequestMapping(value = "/pracownik/edytujPokoj/{id}", method=RequestMethod.GET)     //strona edycji pokoju
    public String edytujPokoj(Model model, @PathVariable("id") Integer id){
        model.addAttribute("PokojIn", pokojRepo.findById(id));
        return "edytujPokoj";
    }

    @RequestMapping(value = "/pracownik/edytujPokoj", method=RequestMethod.POST)     
    public String edytujPokoj(Model model,Pokoj pokoj){
        pokojRepo.save(pokoj);                                                              //zapisujemy edytowany pokój
        model.addAttribute("PokojIn", pokoj);
        return "edytujPokoj";
    }

    @RequestMapping(value = "/pracownik/historiaPokoj/{id}", method=RequestMethod.GET)     
    public String historiaPokoj(Model model, @PathVariable("id") Integer id){                   //historia wynajmu pokoju
        model.addAttribute("historia", rezerwacjaRepo.findAllByPokojId(id));
        return "historiaPokoj";
    }

    @RequestMapping(value = "/pracownik/historiaGosc/{id}", method=RequestMethod.GET)     
    public String historiaGosc(Model model, @PathVariable("id") Integer id){                    //historia wynajmu przez gościa
        model.addAttribute("historia", rezerwacjaRepo.findAllByGoscId(id));
        return "historiaPokoj";
    }

    @RequestMapping(value = "/pracownik/wyswietlRezerwacje", method=RequestMethod.GET)       //wszystkie rezerwacje
    public String wyswietlRezerwacje(Model model){

        model.addAttribute("rezerwacjaTab", rezerwacjaRepo.findAll());
        return "wyswietlRezerwacja";
    }
    @RequestMapping(value = "/pracownik/edytujRezerwacje", method=RequestMethod.GET)        //edytuj rezerwacje o podanym id, wysyła dane do edycji w formie
    public String edytujRezerwacje( @RequestParam(value="ID", defaultValue="0") String ID,
                                    Model model){
        Integer id = Integer.parseInt(ID);
        model.addAttribute("rezerwacja", rezerwacjaRepo.findById(id));
        return "edytujRezerwacje";
    }

    @RequestMapping(value = "/pracownik/edytujRezerwacje", method=RequestMethod.POST)       //zapis edytowanej rezerwacji
    public String edytujRezerwacje(Model model, Rezerwacja rezerwacja){
        model.addAttribute("rezerwacja", rezerwacja);
        rezerwacjaRepo.save(rezerwacja);
        return "edytujRezerwacje";
    }

    


    //logowanie            -------------------------------------------------------------------------------------
    @RequestMapping(value="/login", method=RequestMethod.GET)   //wyświetlane customowego formularza logowania
    public String zaloguj(Model model){
        return("login");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String logincase(Model model, Authentication auth,HttpSession session){
        if(auth == null){   
            model.addAttribute("pokojTab", pokojRepo.findAll());   //jak nie zalogowany to na strone główną
            return "homeAnon";
        }
        else if(auth.getAuthorities().toString().equals("[PRACOWNIK]")){        //jak pracownik to na stronę główną pracownik
            return "homePracownik";
        }
        else if(auth.getAuthorities().toString().equals("[GOSC]")){
            model.addAttribute("pokojTab", pokojRepo.findAll());    ////jak gosc to na stronę główną goscia
            return "homeGosc";
        }
        else{
            return "homeAnon";
        }
    }

    @RequestMapping(value="/accessDenied", method=RequestMethod.GET)            //strona wyświetlana gdy ktoś nie ma uprawnień do jakiejś strony
    public String accessDenied(Model model){
        return("accessDenied");
    }


    //WYJĄTKI              -------------------------------------------------------------------------------------
    @ExceptionHandler
    public String handlerException(Model model,Exception exception)
    {
        String message = exception.getMessage();
        model.addAttribute("errormessage", message);
        return "errorpage";
    }
}
