/**
 * @author Elzbieta Wisniewska and Adam Chojnacki
 */
package def;

public interface GameRulesInterface
{
    int BoardSize = 17;
    int NumberOfPoolsInEnemyBase = 10;
    PlayerId[][] setUpperPools(PlayerId[][] pools);
    PlayerId[][] setUpperLeftPools(PlayerId[][] pools);
    PlayerId[][] setUpperRightPools(PlayerId[][] pools);
    PlayerId[][] setBottomLeftPools(PlayerId[][] pools);
    PlayerId[][] setBottomRightPools(PlayerId[][] pools);
    PlayerId[][] setBottomPools(PlayerId[][] pools);

    PlayerId[][] setBoardForTwoPlayers(PlayerId[][] pools);

    PlayerId[][] setBoardForThreePlayers(PlayerId[][] pools);
    PlayerId[][] setBoardForFourPlayers(PlayerId[][] pools);
    PlayerId[][] setBoardForSixPlayers(PlayerId[][] pools);

    PlayerId[][] setUpBoardForPlayers(NumberOfPlayers numberOfPlayers, PlayerId[][] pools);
}
