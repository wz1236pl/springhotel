package springboot.hotele;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springboot.hotele.models.Gosc;
import springboot.hotele.models.Pokoj;
import springboot.hotele.models.Rezerwacja;
import springboot.hotele.repository.GoscRepo;
import springboot.hotele.repository.PokojRepo;
import springboot.hotele.repository.RezerwacjaRepo;

//          TO DO:
//      - Wyświetlane z parametrem (historia pokoi itp)
//      - Realizowanie pełnej rezerwacji (dodanie info i gościu i rezerwaji na jednym formie)
//      - 

@Controller
public class Kontrolery {
 
    @Autowired
    private GoscRepo goscRepo;
    @Autowired
    private PokojRepo pokojRepo;
    @Autowired
    private RezerwacjaRepo rezerwacjaRepo;

    //pokoje            -------------------------------------------------------------------------------------

    @RequestMapping(value="/dodajPokoj", method=RequestMethod.GET)
    public String dodajPokoj(Model model){
        model.addAttribute("PokojIn", new Pokoj());
        return("nowyPokoj");
    }

    @RequestMapping(value="/dodajPokoj", method=RequestMethod.POST)
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

    @RequestMapping(value="/dodajGosc", method=RequestMethod.GET)
    public String dodajGosc(Model model){
        model.addAttribute("GoscIn", new Gosc());
        return("nowyGosc");
    }

    @RequestMapping(value="/dodajGosc", method=RequestMethod.POST)
    public String dodajGosc(Model model, Gosc gosc){
        goscRepo.save(gosc);

        return("redirect:/nowyGosc");
    }

    @RequestMapping(value = "/wyswietlGosc", method=RequestMethod.GET)
    public String wyswietlGosc(Model model){
        model.addAttribute("goscTab", goscRepo.findAll());
        return "wyswietlGosc";
    }


    //rezerwacja        -------------------------------------------------------------------------------------
    
    @RequestMapping(value="/dodajRezerwacja", method=RequestMethod.GET)
    public String dodajRezerwacja(Model model){
        model.addAttribute("RezerwacjaIn", new Rezerwacja());
        return("nowaRezerwacja");
    }

    @RequestMapping(value="/dodajRezerwacja", method=RequestMethod.POST)
    public String dodajRezerwacja(Model model, Rezerwacja rezerwacja){
        rezerwacjaRepo.save(rezerwacja);

        return("redirect:/nowaRezerwacja");
    }

    @RequestMapping(value = "/wyswietlRezerwacja", method=RequestMethod.GET)
    public String wyswietlRezerwacja(Model model){
        model.addAttribute("goscTab", rezerwacjaRepo.findAll());
        return "wyswietlRezerwacja";
    }

    //dodawanie rezerwacji -------------------------------------------------------------------------------------

    @RequestMapping(value="/rezerwoj", method=RequestMethod.GET)
    public String rezerwoj(Model model ){
        model.addAttribute("GoscIn", new Gosc());
        model.addAttribute("RezerwacjaIn", new Rezerwacja());
        

        return("");
    }

    @RequestMapping(value="/rezerwoj", method=RequestMethod.GET)
    public String rezerwoj(Model model, Gosc gosc, Rezerwacja rezerwacja){
        

        return("");
    }


    //WYJĄTKI           -------------------------------------------------------------------------------------
    @ExceptionHandler
    public String handlerException(Model model,Exception exception)
    {
        String message = exception.getMessage();
        model.addAttribute("errormessage", message);
        return "errorpage";
    }
}
