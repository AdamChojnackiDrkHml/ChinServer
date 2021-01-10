package def;

public class StandardGamePools implements PlayerPoolsInteface
{
    int[][] UpperPools = {{0,8}, {1,7}, {1,8}, {2,7}, {2,8}, {2,9}, {3,6}, {3,7}, {3,8}, {3,9}};
    int[][] UpperLeftPools = {{4,2}, {4,3}, {4,4}, {4,5}, {5,2}, {5,3}, {5,4}, {6,3}, {6,4}, {7,3}};
    int[][] UpperRightPools = {{4,11}, {4,12}, {4,13}, {4,14}, {5,11}, {5,12}, {5,13}, {6,12}, {6,13}, {7,12}};
    int[][] BottomLeftPools = {{9,3}, {10,3}, {10,4}, {11,2}, {11,3}, {11,4}, {12,2}, {12,3}, {12,4}, {12,5}};
    int[][] BottomRightPools = {{12,11}, {12,12}, {12,13}, {12,14}, {11,11}, {11,12}, {11,13}, {10,12}, {10,13}, {9,12}};
    int[][] BottomPools = {{16,8}, {15,7}, {15,8}, {14,7}, {14,8}, {14,9}, {13,6}, {13,7}, {13,8}, {13,9}};

    public PlayerId[][] setUpperPools(PlayerId[][] pools)
    {
        for (int[] cords : UpperPools)
        {
            pools[cords[0]][cords[1]] = PlayerId.FOUR;
        }
        return pools;
    }

    public PlayerId[][] setUpperLeftPools(PlayerId[][] pools)
    {
        for (int[] cords : UpperLeftPools)
        {
            pools[cords[0]][cords[1]] = PlayerId.THREE;
        }
        return pools;
    }
    public PlayerId[][] setUpperRightPools(PlayerId[][] pools)
    {
        for (int[] cords : UpperRightPools)
        {
            pools[cords[0]][cords[1]] = PlayerId.FIVE;
        }
        return pools;
    }
    public PlayerId[][] setBottomLeftPools(PlayerId[][] pools)
    {
        for (int[] cords : BottomLeftPools)
        {
            pools[cords[0]][cords[1]] = PlayerId.TWO;
        }
        return pools;
    }
    public PlayerId[][] setBottomRightPools(PlayerId[][] pools)
    {
        for (int[] cords : BottomRightPools)
        {
            pools[cords[0]][cords[1]] = PlayerId.SIX;
        }
        return pools;
    }
    public PlayerId[][] setBottomPools(PlayerId[][] pools)
    {
        for (int[] cords : BottomPools)
        {
            pools[cords[0]][cords[1]] = PlayerId.ONE;
        }
        return pools;
    }

    public PlayerId[][] setBoardForTwoPlayers(PlayerId[][] pools)
    {
        return setBottomPools(setUpperPools(pools));
    }

    public PlayerId[][] setBoardForThreePlayers(PlayerId[][] pools)
    {
        return setBottomPools(setUpperLeftPools(setUpperRightPools(pools)));

    }
    public PlayerId[][] setBoardForFourPlayers(PlayerId[][] pools)
    {
        return setUpperRightPools(setBottomLeftPools(setBoardForTwoPlayers(pools)));

    }
    public PlayerId[][] setBoardForSixPlayers(PlayerId[][] pools)
    {
        return setBoardForFourPlayers(setUpperLeftPools(setBottomRightPools(pools)));
    }

    public PlayerId[][] setUpBoardForPlayers(NumberOfPlayers numberOfPlayers, PlayerId[][] pools)
    {
        switch (numberOfPlayers)
        {
            case TWO:
            {
                return setBoardForTwoPlayers(pools);
            }
            case THREE:
            {
                return setBoardForThreePlayers(pools);
            }
            case FOUR:
            {
                return setBoardForFourPlayers(pools);
            }
            case SIX:
            {
                return setBoardForSixPlayers(pools);
            }
            default:
            {
                return setBottomPools(pools);
            }
        }
    }
}
