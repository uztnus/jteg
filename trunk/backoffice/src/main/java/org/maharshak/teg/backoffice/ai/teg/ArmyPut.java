package org.maharshak.teg.backoffice.ai.teg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.maharshak.teg.board.Card;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.request.PutArmyRequest;
import org.maharshak.teg.player.Player;


public class ArmyPut {

  public static int tryConquerContinent(Country c, Player p) {
    Continent cont = c.getContinent();
    if (p.continentsOwned().contains(cont))
      return 0;
    int pc = p.getGame().getBoard().getCountries(cont).size();
    int pe = 0; /* enemy countries */
    int ee = 0; /* enemy armies */
    int pm = 0; /* my countries */
    int em = 0; /* my armies */
    int ple = 0; /* countries limitrofes enemigos - countries bordering enimies */


    TegGame g = p.getGame();
    List<Country> countriesInCont = g.getBoard().getCountries(cont);
    for (Country cInCont : countriesInCont) {
      if (g.getOwner(cInCont) == p) {
        pm++;
        em += g.getArmies(cInCont);
      } else {
        pe++;
        ee += g.getArmies(cInCont);
        if (g.getBoard().haveBorders(c, cInCont)) {
          ple++;
        }
      }
    }

    int diff = 0;
    /* si tengo mas del xx% de los countries del cont suma puntos */
    /* If I have more than xx% of the sum cont Countries of points */
    if (pc != pm) {
      /* ASIA */
      if (pc >= 12) {
        if (pm >= 10)
          diff += 1;
        if (pm >= 11)
          diff += 2;
        if (pm >= 12)
          diff += 7;
        if (pm < 7)
          diff -= 5;
      }
      /* EUROPE, NORTH AMERICA */
      else if (pc >= 8) {
        if (pm >= 5)
          diff += 1;
        if (pm >= 7)
          diff += 2;
        if (pm >= 8)
          diff += 7;
      }
      /* SOUTH AMERICA, AFRICA */
      else if (pc >= 6) {
        if (pm >= 4)
          diff += 2;
        if (pm >= 5)
          diff += 7;
      }
      /* OCEANIA */
      if (pc >= 4) {
        if (pm >= 2)
          diff += 2;
        if (pm >= 3)
          diff += 3;
      }
      diff += ple;
      if (em > ee)
        diff += 3;

      if (ple == 0)
        diff -= 50;
    }

    return diff;

  }

  public static int tryToDefendContinet(Country country, Player p, List<String> explain) {



    Continent c = country.getContinent();
    int suma = 0;
    int f = 0; /* fronteras que tiene */

    if (!p.continentsOwned().contains(c))
      return 0;

    TegGame g = p.getGame();
    for (Country country2 : g.getBoard().getCountries()) {
      if (g.getBoard().haveBorders(country, country2) && country2.getContinent() != c) {
        if (g.getOwner(country2) != p) {

          suma += 5;
          explain.add("has enemy at " + country2.getName() + " +5");

          /* increase points if the enemy has more armies */
          if (g.getArmies(country2) > g.getArmies(country)) {
            suma += 6;
            explain.add("enemy at " + country2.getName() + "  has more armies +6");
          }
        } else {
          suma += 1;
          explain.add("has goood neigh " + country2.getName() + "   +1");
        }

        /* increase points if I have a few armies */
        if (g.getArmies(country) < 4) {
          suma += 3;
          explain.add("not enough armies " + country2.getName() + "   +3");
        }

        f++;
      }

    }


    int diff = 0;
    /* if it is no border, dont place on him */
    if (f == 0) {
      diff -= 50;
      explain.add("inner country   -50");
    }

    diff += suma + (f / 2);
    explain.add("sum=" + suma + " | f=" + f + " diff=" + diff);
    return diff;
  }

  public static int tryConquerCountries(Country country, Player p) {
    int p1 = 0;
    int p2 = 0;
    int suma = 0;

    int countriesInCont = p.getGame().getBoard().getCountries(country.getContinent()).size();
    TegGame g = p.getGame();
    /* suma las fichas de los countries limitrofes enemigos */
    for (Country other : g.getBoard().getCountries()) {
      if (g.getOwner(other) != p && g.getBoard().haveBorders(country, other)) {
        /* si es un enemigo ya tiene punto */
        suma += 3;
        if (g.getArmies(country) >= g.getArmies(other))
          p2 += 2 + g.getArmies(country) - g.getArmies(other);
        else
          p1 += g.getArmies(other) - g.getArmies(country);

      }
    }
    if (suma == 0)
      return suma;
    suma += p1 / 2 + p2 / 2;
    /* ASIA */
    if (countriesInCont >= 12)
      suma -= 1;

    /* EUROPE, NORTH AMERICA */
    else if (countriesInCont >= 8)
      suma += 1;

    /* SOUTH AMERICA, AFRICA */
    else if (countriesInCont >= 6)
      suma += 3;

    /* OCENIA */
    if (countriesInCont >= 4)
      suma += 3;

    return suma;

  }

  public static int calculateCoutryStrategy(Country c, Player p, Map<Country, List<String>> explainScore) {
    ArrayList<String> explain = new ArrayList<String>();
    if (explainScore != null) {
      explainScore.put(c, explain);
    }
    int c1 = tryConquerContinent(c, p);
    explain.add("Conquer continent:" + c1);
    int c2 = tryConquerCountries(c, p);
    explain.add("Conquer county:" + c2);
    int c3 = tryToDefendContinet(c, p, explain);
    explain.add("Defend continent:" + c3);
    return c1 + c2 + c3;
  }

  public static List<Card> whatCanIExchange(List<Card> cards, Player p) {

    TegGame game = p.getGame();
    List<Card> myCards = game.getCards(p);
    if (myCards.size() < 3)
      return null;
    for (int i = 0; i < myCards.size(); i++)
      for (int j = i + 1; j < myCards.size(); j++) {
        for (int k = j + 1; k < myCards.size(); k++) {
          if (Card.canExchangeBePerformed(myCards.get(i), myCards.get(j), myCards.get(k))) {
            return Arrays.asList(new Card[] { myCards.get(i), myCards.get(j), myCards.get(k) });
          }
        }
      }
    return null;
  }

  public static int howMuchShouldIPut(Player p) {
    return p.calculateArmiesToPutNoConts() + PutArmyRequest.troopsForContinents(p.continentsOwned());
  }

}
