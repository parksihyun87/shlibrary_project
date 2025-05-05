public class GradeInfo {
    long diffDays;
    int rentCount;

    public GradeInfo(long diffDays, int rentCount) {
        this.diffDays = diffDays;
        this.rentCount = rentCount;
    }

    public long getDiffDays() {
        return diffDays;
    }

    public int getRentCount() {
        return rentCount;
    }
}
