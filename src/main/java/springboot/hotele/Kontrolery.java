package springboot.hotele;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springboot.hotele.models.Gosc;
import springboot.hotele.models.Pokoj;
import springboot.hotele.models.Rezerwacja;
import springboot.hotele.repository.GoscRepo;
import springboot.hotele.repository.PokojRepo;
import springboot.hotele.repository.RezerwacjaRepo;

//          TO DO:
//      - Wyświetlane z parametrem (historia pokoi itp)
//      - Po rejestracji można samodzielnie dokonać rezerwacji pokoju w wybranych datach
//      - th:if taki gość istnieje


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

    //pokoje            -------------------------------------------------------------------------------------

    @RequestMapping(value="/dodajPokoj", method=RequestMethod.GET)      //zmienić na tylko dla pracownikow
    public String dodajPokoj(Model model){
        model.addAttribute("PokojIn", new Pokoj());
        return("nowyPokoj");
    }

    @RequestMapping(value="/dodajPokoj", method=RequestMethod.POST)     //zmienić na tylko dla pracownikow
    public String dodajPokoj(Model model, Pokoj pokoj){
        pokojRepo.save(pokoj);

        return("redirect:/dodajPokoj");
    }
    
    @RequestMapping(value = "/wyswietlPokoj", method=RequestMethod.GET) 
    public String wyswietlPokoje(Model model){
        model.addAttribute("pokojTab", pokojRepo.findAll());
        return "wyswietlPokoj";
    }

    //gosc              -------------------------------------------------------------------------------------

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

    @RequestMapping(value = "/wyswietlGosc", method=RequestMethod.GET)      //zmienić na tylko dla pracownikow
    public String wyswietlGosc(Model model){
        model.addAttribute("goscTab", goscRepo.findAll());
        return "wyswietlGosc";
    }


    //rezerwacja        -------------------------------------------------------------------------------------
    
    @RequestMapping(value = "/wyswietlRezerwacja", method=RequestMethod.GET)        //wszystkie rezerwacje
    public String wyswietlRezerwacja(Model model){
        model.addAttribute("rezerwacjaTab", rezerwacjaRepo.findAll());
        return "wyswietlRezerwacja";
    }

    @RequestMapping(value = "/wyswietlMojeRezerwacje", method=RequestMethod.GET)        //rezerwacje zalogowanego goscia
    public String wyswietlWszystkieRezerwacje(Model model, Authentication authentication){
        model.addAttribute("rezerwacjaTab", rezerwacjaRepo.findAllByGoscEmail(authentication.getName()));
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

    @RequestMapping(value="/rezerwoj1", method=RequestMethod.GET) 
    public String rezerwoj1(Model model ){
        model.addAttribute("GoscIn", new Gosc());
        return("rezerwoj1");
    }

    @RequestMapping(value="/rezerwoj1", method=RequestMethod.POST)
    public String rezerwoj1(Model model, Gosc goscIn){
        goscRepo.save(goscIn);
        return("redirect:/rezerwoj2");
    }

    @RequestMapping(value="/rezerwoj2", method=RequestMethod.GET)
    public String rezerwoj2(Model model ){
        model.addAttribute("rezerwacjaIn", new Rezerwacja());
        model.addAttribute("goscList", goscRepo.findAll());
        model.addAttribute("pokojList", pokojRepo.findAll());
        return("rezerwoj2");
    }

    @RequestMapping(value="/rezerwoj2", method=RequestMethod.POST)
    public String rezerwoj2(Model model, Rezerwacja rezerwacja){
        rezerwacjaRepo.save(rezerwacja);
        model.addAttribute("rezerwacjaIn", new Rezerwacja());
        return("rezerwoj2");
    }

    //logowanie            -------------------------------------------------------------------------------------
    @RequestMapping(value="/login", method=RequestMethod.GET)   //wyświetlane customowego formularza logowania
    public String zaloguj(Model model){
        return("login");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String logincase(Model model, Authentication auth){
        if(auth == null){
            return "homeAnon";
        }
        else if(auth.getAuthorities().toString().equals("[PRACOWNIK]")){
            return "homePracownik";
        }
        else if(auth.getAuthorities().toString().equals("[GOSC]")){
            return "homeGosc";
        }
        else{
            return "homeAnon";
        }
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
