package vincent.pid;

public class SimulPid {

	public static void main(String[] args) {
        Pid pid = new Pid(200, 0.01f, 40000, 25, 30000);

		pid.compute(17);
		pid.compute(17);
		pid.compute(17.5f);
		pid.compute(18);
		pid.compute(18.5f);
		pid.compute(18.5f);
		pid.compute(18.5f);
		pid.compute(18.5f);

		pid.compute(19f);
		pid.compute(19f);
		pid.compute(19f);
		pid.compute(19.5f);

		pid.compute(19.5f);

		pid.compute(21f);
		pid.compute(21.5f);
		pid.compute(22f);
		pid.compute(22f);

	}

}
