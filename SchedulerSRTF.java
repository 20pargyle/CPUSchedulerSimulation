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

        if (cpu.isBurstComplete()){
            contextSwitches++;
            myLogger.log("Process " + cpu.getName() + " burst complete");
            if (cpu.isExecutionComplete()){
                myLogger.log("Process " + cpu.getName() + " execution complete");
            }
            else {
                notifyNewProcess(cpu);
            }

            Process nextProcess = queue.poll();
            if (nextProcess != null){
                contextSwitches++;
                myLogger.log("Scheduled: " + nextProcess.getName());
                return nextProcess;
            }
            else { return null; }
        }


        if (queue.peek() == null || cpu.getRemainingBurst() <= queue.peek().getRemainingBurst()){
            return cpu;
        }
        else {
            queue.add(cpu);
            myLogger.log("Preemptively removed: " + cpu.getName());
            Process nextProcess = queue.poll();
            contextSwitches = contextSwitches + 2;
            return nextProcess;
        }
    }
}

        // 2 approaches:
        //      1) compare cpu to the next shortest item
        //          pros: we still have a reference to the previously run item, list doesn't have to be resorted
        //          cons: uuuuh I don't think there are any
        //      2) place cpu back in the queue, sort, and pull out the shortest
        //          pros: easy to find shortest
        //          cons: have to re-sort the list here in update(), lose reference to original


        //                  | burstComplete   | executionComplete | both  | none  |
        // queue.add(cpu)   |    yes          |    no             | no    | yes   |
        // contextSwitch++  |    yes          |    yes            | no    | no    |
        // then sort queue and return head