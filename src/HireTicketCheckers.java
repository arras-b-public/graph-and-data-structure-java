import java.util.*;

public class HireTicketCheckers {
    private static final int startEndTimeLimit = 100000;


    public int getNumberOfHiredApplicants(int[][] schedules){
        List<Applicant> validApplicants = filterApplicants(schedules);
        Applicant[] potentialTicketCheckers = sortByFinishingTime(validApplicants);//array of applicants we may hire
        int OPT = hireApplicants(potentialTicketCheckers);
        return OPT;
    }//end of getNumberOfHiredApplicants


    private List<Applicant> filterApplicants(int[][] schedules){
        List<Applicant> validApplicants = new ArrayList<>();
        int numberOfApplicants = schedules.length;
        int startTime,endTime;
        
        for (int row = 0; row < numberOfApplicants; row++) {
            startTime = schedules[row][0];
            endTime = schedules[row][1];

            //rule out invalid schedules
            if (Math.abs(startTime) <= startEndTimeLimit && Math.abs(endTime) <= startEndTimeLimit) {
                validApplicants.add(new Applicant(startTime, endTime));
            }
        }

        return validApplicants;
    }//end of filterApplicants


    private Applicant[] sortByFinishingTime(List<Applicant> validApplicants){
        Applicant[] arrayToSort = validApplicants.toArray(new Applicant[0]);
        Arrays.sort(arrayToSort, new ApplicantComparator());
        return arrayToSort;
    }//end of sortByFinishingTime


    private int hireApplicants(Applicant[] potentialTicketCheckers){
        int numberOfApplicantsHired = 1;//always hire the first one
        int indexOfLastApplicantHired = 0;//hired the first one

        for (int i = 1; i < potentialTicketCheckers.length ; i++) {
            if (potentialTicketCheckers[i].startTime >= potentialTicketCheckers[indexOfLastApplicantHired].endTime){
                numberOfApplicantsHired++;
                indexOfLastApplicantHired = i;

            }
        }
        return numberOfApplicantsHired;
    }//end of hireApplicants




    private static class Applicant{
        private final int startTime;
        private final int endTime;

        private Applicant(int startTime, int endTime){
            this.startTime = startTime;
            this.endTime = endTime;
        }//end of Applicant constructor
    }//end of Applicant nested class


    private static class ApplicantComparator implements Comparator<Applicant>{

        @Override
        public int compare(Applicant applicant, Applicant otherApplicant){
            return Integer.compare(applicant.endTime, otherApplicant.endTime);//use default integer comparison
        }//end of compare

    }//end of ApplicantComparator nested class

}//end of HireTicketCheckers class
