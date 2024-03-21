import java.util.*;

public class SchedulerSRTF extends SchedulerBase implements Scheduler {
    Comparator<Process> lengthComparator = new Comparator<Process>() {
        public int compare(Process p1, Process p2) {
            if (p1.getRemainingBurst() < p2.getRemainingBurst())
                return -1;
            return 1;
        }};

    private LinkedList<Process> queue = new LinkedList<Process>();
    Logger myLogger;

    public SchedulerSRTF (Logger myLogger){
        this.myLogger = myLogger;
    }

    @Override
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    @Override
    public void notifyNewProcess(Process p) {
        this.queue.add(p);
        Collections.sort(queue, lengthComparator);
    }

    @Override
    public Process update(Process cpu) {
        if (cpu == null){
            contextSwitches++;
            Process nextProcess = queue.poll();
            myLogger.log("Scheduled: " + nextProcess.getName());
            return nextProcess;
        }
    }

        //                  | burstComplete   | executionComplete | both  | none  |
        // queue.add(cpu)   |    yes          |    no             | no    | yes   |
        // contextSwitch++  |    yes          |    yes            | no    | no    |
        // then sort queue and return head