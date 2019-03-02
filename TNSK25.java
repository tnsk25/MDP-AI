
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TNSK25 {
    private  static final String INPUT = "input.txt";
    private  static final String OUTPUT = "output.txt";
    private static Double[][] box;
    private static double reward;
    private static double discountRate;
    private static double epsilon;
    private static List<Double> probabilityList = new ArrayList<>();
    private static int row = -1, col = -1;
    private static List<Pair> destStates = new ArrayList<>();
    private static Double delta = Double.MIN_VALUE;

    private static boolean isEndState(int i, int j) {
        return destStates.contains(new Pair(i,j));
    }

    public static  void  main(String args[]) throws  Exception{
        List<String> input = readInput();
        setboxSize(input.get(0));
        setboxDefault();
        setBlock(input.get(1));
        setDestStates(input.get(2));
        reward = getDoubleValue(input.get(3));
        setProbabilityList(input.get(4));
        discountRate = getDoubleValue(input.get(5));
        epsilon = getDoubleValue(input.get(6));


        FileWriter fw = new FileWriter(OUTPUT);

        Double[][] prevBox = new Double[row][col];
        int it = 0;

        for( int i =0 ;i<row; i++) {
            for (int j = 0; j < col; j++) {
                if (isEndState(i, j)) {
                    prevBox[i][j] = 0.0;
                    continue;
                }
                prevBox[i][j] = box[i][j];
            }
        }

        do {
            delta = 0.0;
                // Processing states !
            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++) {
                    if (prevBox[i][j] == null) {
//                        prevBox[i][j] = box[i][j];
                        box[i][j] = prevBox[i][j];
                        continue;
                    }
                    if( isEndState(i,j)) continue;
                    Double up = probabilityList.get(0) * utility(prevBox, i, j, i + 1, j) +
                            probabilityList.get(1) * utility(prevBox, i, j, i, j - 1) +
                            probabilityList.get(2) * utility(prevBox, i, j, i, j + 1) +
                            probabilityList.get(3) * utility(prevBox, i, j, i - 1, j);

                    Double down = probabilityList.get(0) * utility(prevBox, i, j, i - 1, j) +
                            probabilityList.get(1) * utility(prevBox, i, j, i, j + 1) +
                            probabilityList.get(2) * utility(prevBox, i, j, i, j - 1) +
                            probabilityList.get(3) * utility(prevBox, i, j, i + 1, j);

                    Double left = probabilityList.get(0) * utility(prevBox, i, j, i, j - 1) +
                            probabilityList.get(1) * utility(prevBox, i, j, i - 1, j) +
                            probabilityList.get(2) * utility(prevBox, i, j, i + 1, j) +
                            probabilityList.get(3) * utility(prevBox, i, j, i, j + 1);

                    Double right = probabilityList.get(0) * utility(prevBox, i, j, i, j + 1) +
                            probabilityList.get(1) * utility(prevBox, i, j, i + 1, j) +
                            probabilityList.get(2) * utility(prevBox, i, j, i - 1, j) +
                            probabilityList.get(3) * utility(prevBox, i, j, i, j - 1);
                    Double max = Math.max(up, Math.max(down, Math.max(left, right)));
                    box[i][j] = max * discountRate + reward;

                }


            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++)
                    if (box[i][j] != null && Math.abs(box[i][j] - prevBox[i][j]) > delta) {
                        delta = Math.abs(box[i][j] - prevBox[i][j]);
                    }
//                    if( isEndState(i,j))
//                        prevBox[i][j] = box[i][j];


            print(fw, it, prevBox);
            it++;

            for( int i =0 ;i<row; i++)
                for (int j = 0; j < col; j++)
                    prevBox[i][j] = box[i][j];

        }while ( delta > epsilon * (1 - discountRate)/ discountRate );

        print(fw, it, prevBox);
        fw.flush();
        fw.close();
    }

    private static void print(FileWriter fw, int it, Double[][] prevBox) throws Exception {
        fw.write("\nIteration: " + it);
        it++;
        for( int i =0 ;i<row; i++) {
            fw.write("\n");
            for (int j = 0; j < col; j++) {
                fw.write(" " +  String.valueOf(prevBox[i][j]));
            }
        }
    }

    private static Double utility(Double[][] prevBox, int curI, int curJ, int i, int j) {
        i = i< 0 ? 0: i;
        i = i == row? row-1 : i;
        j = j < 0  ? 0 : j;
        j = j == col ? col -1  : j;
        if(prevBox[i][j] == null)
            return prevBox[curI][curJ];
        return prevBox[i][j];
    }

    private static void setboxDefault() {
        row = box.length;
        col = box[1].length;
        for(int i = 0 ;i <row; i++) {
            for( int j = 0 ; j<col ; j++) {
                box[i][j] = 0.0;
            }
        }
    }

    private static void setProbabilityList(String data) {
        String[]  probList = data.split(":")[1].split(" ");
        for (String prob : probList) {
            if(prob.trim().equals(""))
                continue;
            probabilityList.add(Double.valueOf(prob.trim()));
        }
    }

    private static Double getDoubleValue(String data) {
        return Double.valueOf(data.split(":")[1].trim());
    }

    private static void setDestStates(String data) {
        String terminalStrList[] = data.split(":")[1].split(",");
        for(String terminalStr  : terminalStrList) {
            String terminalVals[] = terminalStr.split(" ");
            int x = -1, y = -1;
            Double val = -1.0;
            for(String terminalId : terminalVals) {
                if(terminalId.trim().equals(""))
                    continue;
                if(x == -1) {
                    x = Integer.valueOf(terminalId.trim());
                    continue;
                }
                if( y  == -1) {
                    y =  Integer.valueOf(terminalId.trim());
                    continue;
                }
                val = Double.valueOf(terminalId.trim());
            }
            x--;
            y--;
            box[x][y] = val;
            destStates.add(new Pair(x,y));

        }
    }

    private static void setBlock(String data) {
        String blockStrList[] = data.split(":")[1].split(",");
        for(String blockStr : blockStrList) {
            String blockLoc[] = blockStr.split(" ");
            int x = -1, y = -1;
            for(String blockId : blockLoc) {
                if(blockId.trim().equals("")) {
                    continue;
                }
                if(x == -1)
                    x = Integer.valueOf(blockId.trim());
                else
                    y = Integer.valueOf(blockId.trim());
            }
            x--;y--;
            box[x][y] = null;
        }
    }

    private static void setboxSize(String data) {
        int[] sizeArr = new int[2];
        String[] size =  data.split(":")[1].split(" ");
        sizeArr[0] = Integer.valueOf(size[1]);
        sizeArr[1] = Integer.valueOf(size[2]);
//        int[] sizeArr = getSize(input.get(0));
        box = new Double[sizeArr[0]][sizeArr[1]];
    }

    private static List<String> readInput() throws  Exception{
        List<String> data = new ArrayList<>();
        BufferedReader br =  new BufferedReader(new InputStreamReader(new FileInputStream(INPUT)));
        String tmp = null;
        while((tmp = br.readLine())!= null) {
            if(tmp.equals("") || tmp.charAt(0) == '#')
                continue;
            data.add(tmp);
        }
        return data;
    }
}

class Pair {
    private int x;
    private int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        Pair p = (Pair)o;
        return  this.x == p.getX() && this.y == p.getY();
    }

    @Override
    public int hashCode() {
        return (x*31 + y * 23 ) / 119;
    }
}
