import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String path = in.next();
        Long MIN_LENGTH = in.nextLong() * 1048576;

        ForkJoinPool pool = new ForkJoinPool();

        try {

            TreeMap<Long, String> list = pool.invoke(new SearchBigFiles(path, MIN_LENGTH));
            for(Long l: list.descendingKeySet())
            {
                System.out.println(formatFileSize(l) + " " + list.get(l));
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }
}
