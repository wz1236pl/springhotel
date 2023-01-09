package springboot.hotele.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hotele.models.Pokoj;

public interface PokojRepo extends JpaRepository<Pokoj, Integer> {

    Pokoj findByIdIs(Integer id);

    // List<Pokoj> findByRezerwacjaDataStartNotInAndRezerwacjaDataEndNotIn(List<Date> list1,List<Date> list2);
    
    List<Pokoj> findDistinctAllPokojByRezerwacjaDataStartInAndRezerwacjaDataEndIn(List<Date> list1, List<Date> list2);

    List<Pokoj> findAllByIdNotIn(List<Integer> list);

}

//LISTA OPERATOROW DO TWORZENIA ZAPYTAN
/*

IsAfter, After, IsGreaterThan, GreaterThan;
IsGreaterThanEqual, GreaterThanEqual;
IsBefore, Before, IsLessThan, LessThan;
IsLessThanEqual, LessThanEqual;
IsBetween, Between;
IsNull, Null;
IsNotNull, NotNull;
IsIn, In;
IsNotIn, NotIn;
IsStartingWith, StartingWith, StartsWith;
IsEndingWith, EndingWith, EndsWith;
IsContaining, Containing, Contains;
IsLike, Like;
IsNotLike, NotLike;
IsTrue, True;
IsFalse, False;
Is, Equals;
IsNot, Not;
And, Or
OrderBy
*/