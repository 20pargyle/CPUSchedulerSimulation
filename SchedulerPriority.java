import java.util.*;
public class SchedulerPriority extends SchedulerBase implements Scheduler{
    private LinkedList<Process> queue = new LinkedList<Process>();
    Logger myLogger;

    public SchedulerPriority (Logger myLogger){
        this.myLogger = myLogger;
    }

    @Override
    public void notifyNewProcess(Process p) {
        this.queue.add(p);
    }

    @Override
    public Process update(Process cpu) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}
