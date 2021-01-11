/**
 * @author Elzbieta Wisniewska and Adam Chojnacki
 */
package def;

public enum NumberOfPlayers
{
    TWO, THREE, FOUR, SIX;

    public static NumberOfPlayers getFromIntString(String i)
    {
        switch (i)
        {
            case "2":
                return TWO;
            case "3":
                return THREE;
            case "4":
                return FOUR;
            case "5":
                return SIX;
        }
        return TWO;
    }
}