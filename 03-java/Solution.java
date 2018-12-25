import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    public static void main(String[] args) {
        int[][] fabric = new int[1500][1500];

        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            while (line != null) {
                int[] values = parseValues(line);
                int xOffset = values[1],
                    yOffset = values[2],
                    width = values[3],
                    height = values[4];

                // generate the fabric
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        fabric[x + xOffset][y + yOffset]++;
                    }
                }
                line = reader.readLine();
            }

            // count the number of collissions
            int collissions = 0;
            for (int x = 0; x < 1500; x++) {
                for (int y = 0; y < 1500; y++) {
                    if (fabric[x][y] > 1) collissions++;
                }
            }

            System.out.println(collissions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** parse integer values from an input coordinate line
     *
     * @param input line
     * @return [ id, x-coord, y-coord, width, height ]
     */
    private static int[] parseValues(String line) {
        String expression = "^#(.*) @ (.*),(.*): (.*)x(.*)$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) throw new IllegalArgumentException("Something's fucky");

        return new int[]{
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4)),
                Integer.parseInt(matcher.group(5))
        };
    }
}