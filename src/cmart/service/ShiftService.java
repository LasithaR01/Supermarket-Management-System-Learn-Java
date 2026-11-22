package cmart.service;

import cmart.dao.ShiftDao;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;

public class ShiftService {

    private final ShiftDao shiftDao = new ShiftDao();

    public String[] currentOpenShift() throws SQLException {
        SessionContext.require(Permission.SHIFT);
        String user = requireUsername();
        return shiftDao.findOpenShiftRow(user);
    }

    public void open(double openFloat) throws SQLException {
        SessionContext.require(Permission.SHIFT);
        String user = requireUsername();
        if (openFloat < 0) {
            throw new IllegalArgumentException("Opening float cannot be negative.");
        }
        if (shiftDao.hasOpenShift(user)) {
            throw new IllegalArgumentException("You already have an open shift.");
        }
        shiftDao.openShift(user, String.valueOf(openFloat));
    }

    public double[] close(double countedCash) throws SQLException {
        SessionContext.require(Permission.SHIFT);
        String user = requireUsername();
        String[] open = shiftDao.findOpenShiftRow(user);
        if (open == null) {
            throw new IllegalArgumentException("No open shift to close.");
        }
        if (countedCash < 0) {
            throw new IllegalArgumentException("Counted cash cannot be negative.");
        }
        double openFloat = Double.parseDouble(open[3]);
        double salesApprox = shiftDao.sumPaidSalesSince(open[2]);
        double expected = openFloat + salesApprox;
        double diff = countedCash - expected;
        shiftDao.closeShift(Integer.parseInt(open[0]), String.valueOf(countedCash),
                String.valueOf(expected), String.valueOf(diff));
        return new double[] { openFloat, salesApprox, expected, countedCash, diff };
    }

    private String requireUsername() {
        if (SessionContext.currentUser() == null || Validation.isBlank(SessionContext.currentUser().getUsername())) {
            throw new IllegalStateException("Not signed in");
        }
        return SessionContext.currentUser().getUsername();
    }
}
