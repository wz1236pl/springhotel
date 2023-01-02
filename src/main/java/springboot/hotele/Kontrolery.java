package springboot.hotele;

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

//          TO DO:
//      - Wyświetlane z parametrem (historia pokoi itp)
//      - przesyłanie tylko info o pokoju nie obiektu


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
    
    @RequestMapping(value = "/wyswietlPokoj", method=RequestMethod.GET) 
    public String wyswietlPokoje(Model model){
        model.addAttribute("pokojTab", pokojRepo.findAll());
        return "wyswietlPokoj";
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
    public String wyswietlWszystkieRezerwacje(Model model, Authentication authentication){
        model.addAttribute("rezerwacjaTab", rezerwacjaRepo.findAllByGoscEmail(authentication.getName()));
        return "wyswietlRezerwacja";
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
        rezerwacja.setGosc(goscRepo.findByEmail(auth.getName()));
        rezerwacjaRepo.save(rezerwacja);
        return("redirect:/gosc/rezerwuj?dodano");
    }

    //pracownik     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       pracownik

    @RequestMapping(value="/pracownik/dodajPokoj", method=RequestMethod.GET)      //zmienić na tylko dla pracownikow
    public String dodajPokoj(Model model){
        model.addAttribute("PokojIn", new Pokoj());
        return("nowyPokoj");
    }

    @RequestMapping(value="/pracownik/dodajPokoj", method=RequestMethod.POST)     //zmienić na tylko dla pracownikow
    public String dodajPokoj(Model model, Pokoj pokoj){
        pokojRepo.save(pokoj);

        return("redirect:/dodajPokoj");
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
    public String edytujGosc(Model model, Gosc gosc){
        System.out.println(gosc);
        // goscRepo.save(gosc);
        return "redirect:/";
    }

    @RequestMapping(value = "/pracownik/wyswietlRezerwacje", method=RequestMethod.GET)       //wszystkie rezerwacje
    public String wyswietlRezerwacje(Model model){

        model.addAttribute("rezerwacjaTab", rezerwacjaRepo.findAll());
        // System.out.println(rezerwacjaRepo.findAll());
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
