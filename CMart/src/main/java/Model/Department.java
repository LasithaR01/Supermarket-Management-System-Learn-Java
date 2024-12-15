package Model;

public class Department {
    private int deptId;
    private String deptName;
    private String deptType;
    private String deptMgrSsn;

    public Department() {
    }

    public Department(int deptId, String deptName, String deptType, String deptMgrSsn) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptType = deptType;
        this.deptMgrSsn = deptMgrSsn;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getDeptMgrSsn() {
        return deptMgrSsn;
    }

    public void setDeptMgrSsn(String deptMgrSsn) {
        this.deptMgrSsn = deptMgrSsn;
    }
}
