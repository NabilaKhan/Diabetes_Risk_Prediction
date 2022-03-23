package GaussianNaiveBayes;

import java.util.ArrayList;
import java.util.Scanner;

// uses diabetes.csv

/**
 * Created by mmuaz on 12/22/17.
 */
public class MainBSASNaiveBayesPimaJournal {
    public static int calculateInheritanceFactor(String string){
        String[] array = string.split(";");
        for(int i = 0; i < array.length; i++){
            if(array[i].equalsIgnoreCase("father") || array[i].equalsIgnoreCase("mother")){
                return 1;
            }
        }
        return 0;
    }

    public static int calculateGenderFactor(String string){
        if(string.equalsIgnoreCase("male")){
            return 0;
        }
        return 1;
    }

    public static double calculateConditionalProbability(double value, double mean, double variance){
        double ans = 1 / Math.sqrt(2 * Math.PI * variance);
        ans *= Math.exp((-1) * (value - mean) * (value - mean) / (2 * variance));
        return ans;
    }

    public static double square(double a){
        return a * a;
    }

    public static double updateAvg(double avg, double n, double num){
        return (avg * (n - 1) + num) / n;
    }

    public static void main(String[] args) throws Exception{
        ArrayList ageList = new ArrayList();
        ArrayList bmiList = new ArrayList();
        ArrayList pregnantList = new ArrayList();
        ArrayList glucoseList = new ArrayList();
        ArrayList pressureList = new ArrayList();
        ArrayList classList = new ArrayList();

        Scanner input = new Scanner(System.in);

        input.nextLine();

        while(input.hasNextLine()){
            String temp = input.nextLine();
//            System.out.println(temp);
            String store[] = temp.split(",");
//            if(!store[5].equals("")){ //If the sugar level is known
//                System.out.println("bingo");
            double age = Double.valueOf(store[7]);
            double bmi = Double.valueOf(store[5]);
            double pregnant = Double.valueOf(store[3]); // actually skin thickness
            double glucose = Double.valueOf(store[1]);
            double pressure = Double.valueOf(store[2]);
            if(age == 0 || bmi == 0 || pregnant == 0 || glucose == 0 || pressure == 0){
                continue;
            }
            String className = store[8];
            ageList.add(age);
            bmiList.add(bmi);
            pregnantList.add(pregnant);
            glucoseList.add(glucose);
            pressureList.add(pressure);
            classList.add(className);
        }
        int totalCount = classList.size();

        int correctCount = 0;
        double errorSum = 0.0;
        int tp = 0, tn = 0, fp = 0, fn = 0;
        for(int i = 0; i < totalCount; i++){ // leaving one out at each iteration
            //Clustering parameters
            int q = 5; // Maximum number of clusters
            double theta = 17; // Maximum distance to be in same cluster;
            int m = 1; // Initialize the number of clusters

            int testCase = i;
            ArrayList[] clusters = new ArrayList[10];
            double[] clusterAgeAvg = new double[10];
            double[] clusterBmiAvg = new double[10];
            double[] clusterPregnantAvg = new double[10];
            double[] clusterGlucoseAvg = new double[10];
            double[] clusterPressureAvg = new double[10];
            for(int j = 0; j < 10; j++){
                clusters[j] = new ArrayList();
                clusterAgeAvg[j] = 0;
                clusterBmiAvg[j] = 0;
                clusterPregnantAvg[j] = 0;
                clusterGlucoseAvg[j] = 0;
                clusterPressureAvg[j] = 0;
            }

            int testCaseCluster = -1;
            clusters[0].add(0);
            clusterAgeAvg[0] = (double)ageList.get(0);
            clusterBmiAvg[0] = (double)bmiList.get(0);
            clusterPregnantAvg[0] = (double)pregnantList.get(0);
            clusterGlucoseAvg[0] = (double)glucoseList.get(0);
            clusterPressureAvg[0] = (double)pressureList.get(0);
            if(testCase == 0){
                testCaseCluster = 0;
            }
            for(int it = 1; it < totalCount; it++){
                /*if(it != testCase){
                }*/
                double minDistance = 9999999.0;
                int minIndex = -1;
                double age = (double)ageList.get(it);
                double bmi = (double)bmiList.get(it);
                double pregnant = (double)pregnantList.get(it);
                double glucose = (double)glucoseList.get(it);
                double pressure = (double)pressureList.get(it);
                for(int it1 = 0; it1 < m; it1++){
                    double distance = Math.sqrt(square(age - clusterAgeAvg[it1]) + square(bmi - clusterBmiAvg[it1]) + square(pregnant - clusterPregnantAvg[it1])  + square(glucose - clusterGlucoseAvg[it1])  + square(pressure - clusterPressureAvg[it1]) );
                    if(distance < minDistance){
                        minDistance = distance;
                        minIndex = it1;
                    }
                }
//                System.out.println(minDistance);
                if(minDistance > theta && m < q){ // create a new cluster
                    clusters[m].add(it);
                    clusterAgeAvg[m] = (double)ageList.get(it);
                    clusterBmiAvg[m] = (double)bmiList.get(it);
                    clusterPregnantAvg[m] = (double)pregnantList.get(it);
                    clusterGlucoseAvg[m] = (double)glucoseList.get(it);
                    clusterPressureAvg[m] = (double)pressureList.get(it);
                    if(it == testCase){
                        testCaseCluster = m;
                    }
                    m++;
                }
                else{
                    clusters[minIndex].add(it);
                    int clusterSize = clusters[minIndex].size();
                    clusterAgeAvg[minIndex] = updateAvg (clusterAgeAvg[minIndex], clusterSize, (double)ageList.get(it));
                    clusterBmiAvg[minIndex] = updateAvg (clusterBmiAvg[minIndex], clusterSize, (double)bmiList.get(it));
                    clusterPregnantAvg[minIndex] = updateAvg (clusterPregnantAvg[minIndex], clusterSize, (double)pregnantList.get(it));
                    clusterGlucoseAvg[minIndex] = updateAvg (clusterGlucoseAvg[minIndex], clusterSize, (double)glucoseList.get(it));
                    clusterPressureAvg[minIndex] = updateAvg (clusterPressureAvg[minIndex], clusterSize, (double)pressureList.get(it));
                    if(it == testCase){
                        testCaseCluster = minIndex;
                    }
                }
            }

            for(int it = 0; it < m; it++){
                System.out.println(clusters[it]);
            }
            System.out.println(testCaseCluster);

            int diabeticCount = 0;
            int preDiabeticCount = 0;
            int nonDiabeticCount = 0;

            // Metrices for diabetic
            double diabeticAgeSum = 0;
            double diabeticBmiSum = 0;
            double diabeticPregnantSum = 0;
            double diabeticGlucoseSum = 0;
            double diabeticPressureSum = 0;

            double diabeticAgeSqSum = 0;
            double diabeticBmiSqSum = 0;
            double diabeticPregnantSqSum = 0;
            double diabeticGlucoseSqSum = 0;
            double diabeticPressureSqSum = 0;

/*            // Metrices for pre diabetic
            double preDiabeticAgeSum = 0;
            double preDiabeticBmiSum = 0;
            double preDiabeticPregnantSum = 0;
            double preDiabeticGlucoseSum = 0;
            double preDiabeticPressureSum = 0;

            double preDiabeticAgeSqSum = 0;
            double preDiabeticBmiSqSum = 0;
            double preDiabeticPregnantSqSum = 0;
            double preDiabeticGlucoseSqSum = 0;
            double preDiabeticPressureSqSum = 0;*/

            // Metrices for non diabetic
            double nonDiabeticAgeSum = 0;
            double nonDiabeticBmiSum = 0;
            double nonDiabeticPregnantSum = 0;
            double nonDiabeticGlucoseSum = 0;
            double nonDiabeticPressureSum = 0;

            double nonDiabeticAgeSqSum = 0;
            double nonDiabeticBmiSqSum = 0;
            double nonDiabeticPregnantSqSum = 0;
            double nonDiabeticGlucoseSqSum = 0;
            double nonDiabeticPressureSqSum = 0;

            for(int it = 0; it < clusters[testCaseCluster].size(); it++){
                int curIndex = Integer.valueOf(clusters[testCaseCluster].get(it).toString());
                if(curIndex == testCase){
                    continue;
                }
                double age = Double.valueOf(ageList.get(curIndex).toString());
                double bmi = Double.valueOf(bmiList.get(curIndex).toString());
                double pregnant = Double.valueOf(pregnantList.get(curIndex).toString());
                double glucose = Double.valueOf(glucoseList.get(curIndex).toString());
                double pressure = Double.valueOf(pressureList.get(curIndex).toString());
                if(classList.get(curIndex).toString().equalsIgnoreCase("0")){
                    nonDiabeticCount++;
                    nonDiabeticAgeSum += age;
                    nonDiabeticAgeSqSum += (age * age);
                    nonDiabeticBmiSum += bmi;
                    nonDiabeticBmiSqSum += (bmi * bmi);
                    nonDiabeticPregnantSum += pregnant;
                    nonDiabeticPregnantSqSum += (pregnant * pregnant);
                    nonDiabeticGlucoseSum += glucose;
                    nonDiabeticGlucoseSqSum += (glucose * glucose);
                    nonDiabeticPressureSum += pressure;
                    nonDiabeticPressureSqSum += (pressure * pressure);
                }
                /*else if(classList.get(curIndex).toString().equalsIgnoreCase("pre")){
                    preDiabeticCount++;
                    preDiabeticAgeSum += age;
                    preDiabeticAgeSqSum += (age * age);
                    preDiabeticBmiSum += bmi;
                    preDiabeticBmiSqSum += (bmi * bmi);
                    preDiabeticPregnantSum += pregnant;
                    preDiabeticPregnantSqSum += (pregnant * pregnant);
                    preDiabeticGlucoseSum += glucose;
                    preDiabeticGlucoseSqSum += (glucose * glucose);
                    preDiabeticPregnantSum += pregnant;
                    preDiabeticPregnantSqSum += (pregnant * pregnant);
                }*/
                else{
                    diabeticCount++;
                    diabeticAgeSum += age;
                    diabeticAgeSqSum += (age * age);
                    diabeticBmiSum += bmi;
                    diabeticBmiSqSum += (bmi * bmi);
                    diabeticPregnantSum += pregnant;
                    diabeticPregnantSqSum += (pregnant * pregnant);
                    diabeticGlucoseSum += glucose;
                    diabeticGlucoseSqSum += (glucose * glucose);
                    diabeticPressureSum += pressure;
                    diabeticPressureSqSum += (pressure * pressure);
                }
            }
            double diabeticAgeMean = (diabeticAgeSum / diabeticCount);
            double diabeticAgeVariance = (diabeticAgeSqSum / diabeticCount) - (diabeticAgeMean);
            double diabeticBmiMean = (diabeticBmiSum / diabeticCount);
            double diabeticBmiVariance = (diabeticBmiSqSum / diabeticCount) - (diabeticBmiMean);
            double diabeticPregnantMean = (diabeticPregnantSum / diabeticCount);
            double diabeticPregnantVariance = (diabeticPregnantSqSum / diabeticCount) - (diabeticPregnantMean);
            double diabeticGlucoseMean = (diabeticGlucoseSum / diabeticCount);
            double diabeticGlucoseVariance = (diabeticGlucoseSqSum / diabeticCount) - (diabeticGlucoseMean);
            double diabeticPressureMean = (diabeticPressureSum / diabeticCount);
            double diabeticPressureVariance = (diabeticPressureSqSum / diabeticCount) - (diabeticPressureMean);

            /*double preDiabeticAgeMean = (preDiabeticAgeSum / preDiabeticCount);
            double preDiabeticAgeVariance = (preDiabeticAgeSqSum / preDiabeticCount) - (preDiabeticAgeMean);
            double preDiabeticBmiMean = (preDiabeticBmiSum / preDiabeticCount);
            double preDiabeticBmiVariance = (preDiabeticBmiSqSum / preDiabeticCount) - (preDiabeticBmiMean);
            double preDiabeticPregnantMean = (preDiabeticPregnantSum / preDiabeticCount);
            double preDiabeticPregnantVariance = (preDiabeticPregnantSqSum / preDiabeticCount) - (preDiabeticPregnantMean);
            double preDiabeticGlucoseMean = (preDiabeticGlucoseSum / preDiabeticCount);
            double preDiabeticGlucoseVariance = (preDiabeticGlucoseSqSum / preDiabeticCount) - (preDiabeticGlucoseMean);
            double preDiabeticPregnantMean = (preDiabeticPregnantSum / preDiabeticCount);
            double preDiabeticPregnantVariance = (preDiabeticPregnantSqSum / preDiabeticCount) - (preDiabeticPregnantMean);*/

            double nonDiabeticAgeMean = (nonDiabeticAgeSum / nonDiabeticCount);
            double nonDiabeticAgeVariance = (nonDiabeticAgeSqSum / nonDiabeticCount) - (nonDiabeticAgeMean);
            double nonDiabeticBmiMean = (nonDiabeticBmiSum / nonDiabeticCount);
            double nonDiabeticBmiVariance = (nonDiabeticBmiSqSum / nonDiabeticCount) - (nonDiabeticBmiMean);
            double nonDiabeticPregnantMean = (nonDiabeticPregnantSum / nonDiabeticCount);
            double nonDiabeticPregnantVariance = (nonDiabeticPregnantSqSum / nonDiabeticCount) - (nonDiabeticPregnantMean);
            double nonDiabeticGlucoseMean = (nonDiabeticGlucoseSum / nonDiabeticCount);
            double nonDiabeticGlucoseVariance = (nonDiabeticGlucoseSqSum / nonDiabeticCount) - (nonDiabeticGlucoseMean);
            double nonDiabeticPressureMean = (nonDiabeticPressureSum / nonDiabeticCount);
            double nonDiabeticPressureVariance = (nonDiabeticPressureSqSum / nonDiabeticCount) - (nonDiabeticPressureMean);

            System.out.println("Diabetic: " + diabeticCount + ", Pre: " + preDiabeticCount + ", Non: " + nonDiabeticCount);
            System.out.println("Feature\tDiabetic mean\tDiabetic variance\tNon mean\tNon variance");
            System.out.println("Age\t" + diabeticAgeMean + "\t" + diabeticAgeVariance + "\t" + nonDiabeticAgeMean + "\t" + nonDiabeticAgeVariance);
            System.out.println("Bmi\t" + diabeticBmiMean + "\t" + diabeticBmiVariance + "\t" + nonDiabeticBmiMean + "\t" + nonDiabeticBmiVariance);
            System.out.println("Pregnant\t" + diabeticPregnantMean + "\t" + diabeticPregnantVariance + "\t" + nonDiabeticPregnantMean + "\t" + nonDiabeticPregnantVariance);
            System.out.println("Glucose\t" + diabeticGlucoseMean + "\t" + diabeticGlucoseVariance + "\t" + nonDiabeticGlucoseMean + "\t" + nonDiabeticGlucoseVariance);
            System.out.println("Pressure\t" + diabeticPressureMean + "\t" + diabeticPressureVariance + "\t" + nonDiabeticPressureMean + "\t" + nonDiabeticPressureVariance);

            int totalCountInCluster = diabeticCount + preDiabeticCount + nonDiabeticCount;


            double diabeticPrior = (double) diabeticCount / (double) totalCountInCluster;
            double preDiabeticPrior = (double) preDiabeticCount / (double) totalCountInCluster;
            double nonDiabeticPrior = (double) nonDiabeticCount / (double) totalCountInCluster;

            double testAge = (double) ageList.get(testCase);
            double testBmi = (double) bmiList.get(testCase);
            double testPregnant = (double) pregnantList.get(testCase);
            double testGlucose = (double) glucoseList.get(testCase);
            double testPressure = (double) pressureList.get(testCase);
            String testClass = classList.get(testCase).toString();
            double error = 0;

            System.out.println("Test #" + (testCase + 1) + ":");
            System.out.println();
            System.out.println("Age: " + testAge + ",  Bmi: " + testBmi + ",  Pregnant: " + testPregnant  + ",  Glucose: " + testGlucose  + ",  Pressure: " + testPressure );

            // Test for diabetic
            double diabeticAgeConditional = calculateConditionalProbability(testAge, diabeticAgeMean, diabeticAgeVariance);
            double diabeticBmiConditional = calculateConditionalProbability(testBmi, diabeticBmiMean, diabeticBmiVariance);
            double diabeticPregnantConditional = calculateConditionalProbability(testPregnant, diabeticPregnantMean, diabeticPregnantVariance);
            double diabeticGlucoseConditional = calculateConditionalProbability(testGlucose, diabeticGlucoseMean, diabeticGlucoseVariance);
            double diabeticPressureConditional = calculateConditionalProbability(testPressure, diabeticPressureMean, diabeticPressureVariance);
            double diabeticPosteriorNumerator = diabeticPrior * diabeticAgeConditional * diabeticBmiConditional * diabeticPregnantConditional * diabeticGlucoseConditional * diabeticPressureConditional;
            System.out.println("Diabetic: " + diabeticPrior + " " + diabeticAgeConditional + " " + diabeticBmiConditional + " " + diabeticPregnantConditional + " " + diabeticGlucoseConditional + " " + diabeticPressureConditional);
//            System.out.println("P(diabetic): " + diabeticPosteriorNumerator);
/*

            // Test for pre diabetic

            double preDiabeticAgeConditional = calculateConditionalProbability(testAge, preDiabeticAgeMean, preDiabeticAgeVariance);
            double preDiabeticBmiConditional = calculateConditionalProbability(testBmi, preDiabeticBmiMean, preDiabeticBmiVariance);
            double preDiabeticPregnantConditional = calculateConditionalProbability(testPregnant, preDiabeticPregnantMean, preDiabeticPregnantVariance);
            double preDiabeticGlucoseConditional = calculateConditionalProbability(testGlucose, preDiabeticGlucoseMean, preDiabeticGlucoseVariance);
            double preDiabeticPregnantConditional = calculateConditionalProbability(testPregnant, preDiabeticPregnantMean, preDiabeticPregnantVariance);
            double preDiabeticPosteriorNumerator = preDiabeticPrior * preDiabeticAgeConditional * preDiabeticBmiConditional * preDiabeticPregnantConditional;
            System.out.println("pre diabetic: " + preDiabeticPrior + " " + preDiabeticAgeConditional + " " + preDiabeticBmiConditional + " " + preDiabeticPregnantConditional + " " + preDiabeticGlucoseConditional + " " + preDiabeticPregnantConditional);
//            System.out.println("P(pre diabetic): " + preDiabeticPosteriorNumerator);
*/


            // Test for non diabetic

            double nonDiabeticAgeConditional = calculateConditionalProbability(testAge, nonDiabeticAgeMean, nonDiabeticAgeVariance);
            double nonDiabeticBmiConditional = calculateConditionalProbability(testBmi, nonDiabeticBmiMean, nonDiabeticBmiVariance);
            double nonDiabeticPregnantConditional = calculateConditionalProbability(testPregnant, nonDiabeticPregnantMean, nonDiabeticPregnantVariance);
            double nonDiabeticGlucoseConditional = calculateConditionalProbability(testGlucose, nonDiabeticGlucoseMean, nonDiabeticGlucoseVariance);
            double nonDiabeticPressureConditional = calculateConditionalProbability(testPressure, nonDiabeticPressureMean, nonDiabeticPressureVariance);
            double nonDiabeticPosteriorNumerator = nonDiabeticPrior * nonDiabeticAgeConditional * nonDiabeticBmiConditional * nonDiabeticPregnantConditional * nonDiabeticGlucoseConditional * nonDiabeticPressureConditional;
            System.out.println("non diabetic: " + nonDiabeticPrior + " " + nonDiabeticAgeConditional + " " + nonDiabeticBmiConditional + " " + nonDiabeticPregnantConditional + " " + nonDiabeticGlucoseConditional + " " + nonDiabeticPressureConditional);
//            System.out.println("P(non diabetic): " + nonDiabeticPosteriorNumerator);

            double probabilitySum = diabeticPosteriorNumerator + nonDiabeticPosteriorNumerator;
            System.out.println("P(diabetic): " + diabeticPosteriorNumerator / probabilitySum);
//            System.out.println("P(pre diabetic): " + preDiabeticPosteriorNumerator / probabilitySum);
            System.out.println("P(non diabetic): " + nonDiabeticPosteriorNumerator / probabilitySum);

            System.out.println("Diagnosis (real) result: " + testClass);
            String testResult;
            if(nonDiabeticPosteriorNumerator >= diabeticPosteriorNumerator){
                testResult = "0";
                error = (diabeticPosteriorNumerator / probabilitySum);
            }
            /*else if(preDiabeticPosteriorNumerator >= diabeticPosteriorNumerator && preDiabeticPosteriorNumerator >= nonDiabeticPosteriorNumerator){
                testResult = "pre";
                error = (diabeticPosteriorNumerator / probabilitySum) + (nonDiabeticPosteriorNumerator / probabilitySum);
            }*/
            else{
                testResult = "1";
                error = (nonDiabeticPosteriorNumerator / probabilitySum);
            }
            errorSum += error;

            System.out.println("Test Result: " + testResult);
            if(testResult.equalsIgnoreCase(testClass)){
                System.out.println("Prediction: Correct!");
                correctCount++;
            }
            else{
                System.out.println("Prediction: Incorrect!");
            }

            //calculating the evaluation parameters
            if(testResult.equalsIgnoreCase("0") && testClass.equalsIgnoreCase("0")){
                tn++;
            }
            else if(testResult.equalsIgnoreCase("0") && testClass.equalsIgnoreCase("1")){
                fn++;
            }
            else if(testResult.equalsIgnoreCase("1") && testClass.equalsIgnoreCase("1")){
                tp++;
            }
            else if(testResult.equalsIgnoreCase("1") && testClass.equalsIgnoreCase("0")){
                fp++;
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Total test case: " + totalCount);
        System.out.println("Correct assumptions: " + correctCount);
        System.out.println("Probability of Error: " + (errorSum / totalCount));
        System.out.println("True Positive: " + tp);
        System.out.println("True Negative: " + tn);
        System.out.println("False Positive: " + fp);
        System.out.println("False Negative: " + fn);
        System.out.println("Accuracy: " + (tn + tp) / (double) (tn + tp + fn + fp));
        System.out.println("Sensitivity: " + tp / (double) (tp + fn));
        System.out.println("Specificity: " + tn / (double) (tn + fp));

        System.out.println();
        System.out.println();

//        System.out.println("Accuracy: " + (1 - (errorSum / totalCount)));
//        System.out.println("Accuracy of system: " + correctCount / (double) totalCount);
    }
}
