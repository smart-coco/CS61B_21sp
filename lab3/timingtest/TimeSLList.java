package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        int n=8,count=1000,opt,M=10000;
        AList<Integer> N=new AList<>();
        AList<Double> time=new AList<>();
        AList<Integer> opt_array=new AList<>();
        for (int i=1;i<n;i++) {
            //construct
            SLList<Integer> test=new SLList<>();
            for (int j=0;j<count;j++) {
                test.addLast(j);
            }
            //test
            Stopwatch sw = new Stopwatch();
            opt=0;
            for (int j=0;j<M;j++) {
                test.getLast();
                opt+=1;
            }
            //record
            N.addLast(count);
            double timeInSeconds = sw.elapsedTime();
            time.addLast(timeInSeconds);
            opt_array.addLast(opt);
            count*=2;
        }
        printTimingTable(N, time, opt_array);
    }

}
