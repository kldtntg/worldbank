public class RequestModel {

        private final int lowestYear;
        private final int highestYear;

        private final String fileName;


    public RequestModel(int lowestYear, int highestYear, String fileName) {
            this.lowestYear = lowestYear;
            this.highestYear = highestYear;
            this.fileName = fileName;
        }

        public int getLowestYear() {
            return lowestYear;
        }

        public int getHighestYear() {
            return highestYear;
        }

        public String getFileName() {
            return fileName;
        }
}
