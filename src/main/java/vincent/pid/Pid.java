package vincent.pid;

public class Pid {

	/** Coefficients saisis par l'utilisateur */
	/** Coefficient intégral saisis par l'utilisateur */
	private float coefIUtilisateur;
	/** Coefficient dérivé saisis par l'utilisateur */
	private float coefDUtilisateur;

	/** Coefficients corrigés en fonction de la période d'échantillonnage */
	private float coefP;
	private float coefI;
	private float coefD;

	private float sortieMin = 0;
	private float sortieMax = 2000;
	private float consigne;
	private int periodeMs;

	private float termeIntegral;
	private float dernierEntree;
	private boolean premierLancement = true;

	/**
	 * @param coefP
	 * @param coefI
	 * @param coefD
	 * @param entree
	 * @param sortie
	 * @param consigne
	 * @param periodeEchantillonnageMs périodeEchantillonage en Ms
	 */
	public Pid(float coefP, float coefI, float coefD, float consigne, int periodeEchantillonnageMs) {
		super();
		periodeMs = periodeEchantillonnageMs;
		this.consigne = consigne;
		definirCoefs(coefP, coefI, coefD);
	}

	public float compute(float entree) {
		if (premierLancement) {
			dernierEntree = entree;
			premierLancement = false;
		}
		/* Compute all the working error variables */

		float error = consigne - entree;
		termeIntegral += (coefI * error);
		if (termeIntegral > sortieMax)
			termeIntegral = sortieMax;
		else if (termeIntegral < sortieMin)
			termeIntegral = sortieMin;
		float dInput = (entree - dernierEntree);

		/* Compute PID Output */
		float output = coefP * error + termeIntegral - coefD * dInput;

		if (output > sortieMax)
			output = sortieMax;
		else if (output < sortieMin)
			output = sortieMin;

		/* Remember some variables for next time */
		dernierEntree = entree;
		System.out.println(output);
		return output;
	}

	public void definirBornes(float min, float max) {
		sortieMin = min;
		sortieMax = max;
	}

	public void definirCoefs(float coefP, float coefI, float coefD) {
		if (coefP < 0 || coefI < 0 || coefD < 0)
			return;

		coefIUtilisateur = coefI;
		coefDUtilisateur = coefD;

		float sampleTimeInSec = periodeMs / 1000;
		this.coefP = coefP;
		this.coefI = coefI * sampleTimeInSec;
		this.coefD = coefD / sampleTimeInSec;

	}

	public void definirEchantillonnage(int periodeMs) {
		double ratio = (double) periodeMs / (double) this.periodeMs;
		coefI *= ratio;
		coefD /= ratio;
	}
}
