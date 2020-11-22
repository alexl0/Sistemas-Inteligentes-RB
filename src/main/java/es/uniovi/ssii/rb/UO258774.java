package es.uniovi.ssii.rb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.model.network.EvidenceCase;
import org.openmarkov.core.model.network.Finding;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.gui.dialog.io.NetsIO;
import org.openmarkov.inference.likelihoodWeighting.LogicSampling;

// This class carries out evidence propagation on a given network printing out
// the time taken by different algorithms
public class UO258774 {

	private static ProbNet probNet;

	public static void main(String[] args) throws Exception {

		UO258774 obj = new UO258774("asia.pgmx");

		System.out.format("Network \"%s\" with %d nodes and %d links\n", obj.getProbNet().getName(),
				obj.getProbNet().getNumNodes(), obj.getProbNet().getLinks().size());

		//Evidencia
		EvidenceCase evidence = new EvidenceCase();
		Variable variable = probNet.getVariables().get(5);
		Variable variable2 = probNet.getVariables().get(0);
		evidence.addFinding(probNet, variable.getName(), "no");
		evidence.addFinding(probNet, variable2.getName(), "no");

		//Variables de interés
		List<Variable> variablesOfInterest = new ArrayList<Variable>();
		List<Variable> possibleVariables = probNet.getVariables();
		variablesOfInterest.add(possibleVariables.get(7));


		System.out.println("######## Inferencia aproximada ########");
		System.out.println("#### Muestreo estocastico (LogicSampling) ####");

		obj.LSInference(variablesOfInterest, evidence);


	}

	public UO258774(String fileName) throws Exception {
		probNet = NetsIO.openNetworkFile("src/main/resources/networks/"+fileName).getProbNet();
	}

	public ProbNet getProbNet() {
		return probNet;
	}

	@SuppressWarnings("static-access")
	public void setProbNet(ProbNet probNet) {
		this.probNet = probNet;
	}

	public long LSInference(List<Variable> variablesOfInterest, EvidenceCase evidence) {

		LogicSampling propagation = null;
		try {
			propagation = new LogicSampling(probNet);
		} catch (NotEvaluableNetworkException e) {
			e.printStackTrace();
		}
		propagation.setSampleSize(10000);
		propagation.setVariablesOfInterest(variablesOfInterest);

		propagation.setPostResolutionEvidence(evidence);

		System.out.print("Muestreo estocastico (LogicSample)\n");
		long startTime = System.nanoTime();
		try {
			Map<Variable, TablePotential> posteriorProbabilities = propagation.getPosteriorValues();
			printProbabilities(evidence, variablesOfInterest, posteriorProbabilities);

		} catch (IncompatibleEvidenceException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		long endTime = System.nanoTime();

		printTime(endTime - startTime);

		return (endTime - startTime);
	}

	public static void printProbabilities(EvidenceCase evidence, List<Variable> variablesOfInterest,
			Map<Variable, TablePotential> posteriorProbabilities) {

		String evidenceString = "";
		for (Finding finding : evidence.getFindings()) {
			evidenceString += " " + finding.getVariable() + "=" + finding.getState();
		}

		for (Variable variable : variablesOfInterest) {
			TablePotential posteriorProbabilitiesPotential = posteriorProbabilities.get(variable);
			System.out.format("P( %s=%s | %s) = %.5f\n", variable, variable.getStates()[1].getName(), evidenceString,
					1-posteriorProbabilitiesPotential.values[0]);
		}
	}

	public static void printTime(long nanoseconds) {
		System.out.format("Total time: %.2f miliseconds\n", nanoseconds / 1000000.0);
	}

}
