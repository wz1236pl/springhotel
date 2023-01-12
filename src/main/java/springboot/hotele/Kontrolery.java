package springboot.hotele;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    //all       +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       all

    @RequestMapping(value="/pokoje", method=RequestMethod.GET)
    public String pokojedata(Model model){

        model.addAttribute("start", new Date(0));
        model.addAttribute("end", new Date(0));

        return("data");
    }
    @RequestMapping(value="/pokoje", method=RequestMethod.POST)
    public String pokoje(Model model, @ModelAttribute("start") Date start, @ModelAttribute("end") Date end ){
        System.out.println(start);
        System.out.println(end);
        // model.addAttribute("pokojTab", pokojRepo.findAllByRezerwacjaNotBetween(null, null));
        return("wyswietlPokoj");
    }
    
    @RequestMapping(value = "/wyswietlPokoj", method=RequestMethod.GET) 
    public String wyswietlPokoje(Model model, Authentication auth){
        model.addAttribute("pokojTab", pokojRepo.findAll());
        if(auth==null){
            return "wyswietlPokoj";
        }else if(auth.getAuthorities().toString().equals("[PRACOWNIK]")){
            return "wyswietlPokojPracownik";
        }else{
            return "wyswietlPokoj";
        }
    }

    @RequestMapping(value="/register", method=RequestMethod.GET)
    public String dodajGosc(Model model){
        model.addAttribute("GoscIn", new Gosc());
        return("nowyGosc");
    }

    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String dodajGosc(Model model, Gosc gosc){
        if(goscRepo.findByEmail(gosc.getEmail())!=null){
            return "redirect:/register?userexist";
        }
        try{
            gosc.setDokument(gosc.getDokument().replaceAll("\\s+",""));
            gosc.setPassword(passwordEncoder.encode(gosc.getPassword()));
            gosc.setRole("GOSC");
            goscRepo.save(gosc);
            return("redirect:/login?registered");  
        }catch(Exception e){
            System.out.println(e.getMessage());
            return "redirect:/register?error";
        }
    }

    //gosc          +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       gosc

    @RequestMapping(value = "/gosc/wyswietlMojeRezerwacje", method=RequestMethod.GET)        //rezerwacje zalogowanego goscia
    public String wyswietlWszystkieRezerwacje(Model model, Authentication auth){
        model.addAttribute("rezerwacjaTab", rezerwacjaRepo.findAllByGoscEmail(auth.getName()));
        return "wyswietlRezerwacja";
    }

    // @RequestMapping(value="/gosc/rezerwuj/{id}", method=RequestMethod.GET)
    // public String rezerwuj(Model model, Authentication auth, @PathVariable("id") Integer id){
    //     try {
    //         Gosc gosc = goscRepo.findByEmail(auth.getName());
    //         Pokoj pokoj = pokojRepo.findByIdIs(id);
    //         Rezerwacja rezerwacja = new Rezerwacja();
    //         rezerwacja.setGosc(gosc);
    //         rezerwacja.setPokoj(pokoj);
    //         model.addAttribute("goscInfo", gosc.getImie()+" "+gosc.getNazwisko()+" "+gosc.getEmail());
    //         model.addAttribute("rezerwacjaIn", rezerwacja);
    //         model.addAttribute("pokojInfo", "Nr.: "+pokoj.getNrPokoju()+" opis: "+pokoj.getOpis());
    //         System.out.println(rezerwacja);
    //         return("rezerwujId");
    //     } catch (Exception e) {
    //         throw e;
    //     }
    // }

    // @RequestMapping(value="/gosc/rezerwuj", method=RequestMethod.POST)
    // public String rezerwuj(Model model, Rezerwacja rezerwacja, Authentication auth){
    //     try {
    //         // rezerwacja.setGosc(goscRepo.findByEmail(auth.getName()));
    //         // Pokoj pokoj = pokojRepo.findByIdIs(id);
    //         // rezerwacja.setPokoj(pokoj);
    //         // rezerwacjaRepo.save(rezerwacja);
    //         System.out.println(rezerwacja);
    //         model.addAttribute("rezerwacjaIn", rezerwacja);
    //         return("rezerwujId");
    //     } catch (Exception e) {
    //         return("");
    //     }
    // }


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
    public String edytujDane(Model model, Authentication auth, Gosc nowyGosc){
        try {
            Gosc staryGosc = goscRepo.findByEmail(auth.getName());
            nowyGosc.setId(staryGosc.getId());
            nowyGosc.setRole(staryGosc.getRole());
            nowyGosc.setPassword(staryGosc.getPassword());
            goscRepo.save(nowyGosc);
            return "redirect:/gosc/edytujDane?success";   
        } catch (Exception e) {
            return "redirect:/gosc/edytujDane?error";
        }
        
    }

    //pracownik     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       pracownik

    @RequestMapping(value="/pracownik/dodajPokoj", method=RequestMethod.GET)      
    public String dodajPokoj(Model model){
        model.addAttribute("PokojIn", new Pokoj());
        return("nowyPokoj");
    }

    @RequestMapping(value="/pracownik/dodajPokoj", method=RequestMethod.POST)     
    public String dodajPokoj(Model model, Pokoj pokoj){
        pokojRepo.save(pokoj);
        return("redirect:/pracownik/dodajPokoj");
    }
    
    @RequestMapping(value = "/pracownik/wyswietlGosc", method=RequestMethod.GET)     
    public String wyswietlGosc(Model model){
        model.addAttribute("goscTab", goscRepo.findAll());
        return "wyswietlGosc";
    }

    @RequestMapping(value = "/pracownik/edytujGosc/{id}", method=RequestMethod.GET)     
    public String edytujGosc(Model model, @PathVariable("id") Integer id){
        model.addAttribute("GoscIn", goscRepo.findById(id));
        return "edytujGosc";
    }

    @RequestMapping(value = "/pracownik/edytujGosc", method=RequestMethod.POST)     
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

    @RequestMapping(value = "/pracownik/edytujPokuj/{id}", method=RequestMethod.GET)     
    public String edytujPokoj(Model model, @PathVariable("id") Integer id){
        model.addAttribute("PokojIn", pokojRepo.findById(id));
        return "edytujPokuj";
    }
    @RequestMapping(value = "/pracownik/edytujPokuj", method=RequestMethod.POST)     
    public String edytujPokoj(Model model,Pokoj pokoj){
        pokojRepo.save(pokoj);
        model.addAttribute("PokojIn", pokoj);
        return "edytujPokuj";
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
    public String logincase(Model model, Authentication auth){
        if(auth == null){
            model.addAttribute("auth",auth);
            model.addAttribute("pokojTab", pokojRepo.findAll());
            return "homeAnon";
        }
        else if(auth.getAuthorities().toString().equals("[PRACOWNIK]")){
            return "homePracownik";
        }
        else if(auth.getAuthorities().toString().equals("[GOSC]")){
            model.addAttribute("pokojTab", pokojRepo.findAll());
            return "homeGosc";
        }
        else{
            return "homeAnon";
        }
    }
    //testy                --------------------------------------------------------------------------------------
    
    @RequestMapping(value="/testdaty", method=RequestMethod.GET)   
    public String testdaty(Model model){
        return("data");
    }

    @RequestMapping(value="/testdaty", method=RequestMethod.POST)   
    public String testdaty(Model model,Date start, Date end){
        Long startLong = start.getTime();
        Long endLong = end.getTime();
        List<Date> listaDat1= new ArrayList<>();
        List<Integer> listaZajete= new ArrayList<>();
        for(Long i=startLong;i<=endLong;i+=86400000){
            listaDat1.add(new Date(i));
        }
        List<Date> listaDat2= new ArrayList<>(listaDat1);
        listaDat1.remove(listaDat1.size()-1);
        listaDat2.remove(0);
        List<Pokoj> zajete= pokojRepo.findDistinctAllPokojByRezerwacjaDataStartInAndRezerwacjaDataEndIn(listaDat1, listaDat2);
        listaZajete.add(0);
        for(Pokoj p:zajete){
            listaZajete.add(p.getId());
        }
        model.addAttribute("pokojTab",pokojRepo.findAllByIdNotIn(listaZajete));
        
        return("wyswietlPokoj");
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
