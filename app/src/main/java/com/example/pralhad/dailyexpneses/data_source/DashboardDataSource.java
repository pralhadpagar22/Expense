package com.example.pralhad.dailyexpneses.data_source;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardDataSource {
    private MainDataSource dataSource;

    public DashboardDataSource(MainDataSource mainDataSource) {
        this.dataSource = mainDataSource;
    }

    //Graph Group Bar Chart monthly invoice
    public Map<String, Object> getBarChartData(String year) {
        Map<String, Double> inPaid = new HashMap<>();
        Map<String, Double> inDue = new HashMap<>();
        // Cursor cursor = dataSource.rawQuery("SELECT strftime('%m-%Y', inDate) AS monthYear, sum(inPaid) as inPaid, sum(inDue) as inDue from (select inDate, inDue, inPaid, 0 as syncId from invoices where inDate IS NOT NULL AND strftime('%Y', inDate) =  '" + year  + "' AND isActive = 1 AND inTypeId = " + inTypeId + " AND cmpId = " + cmpId + " UNION ALL select inDate, inDue, inPaid, syncId from off_invoices where inDate IS NOT NULL  AND strftime('%Y', inDate) = '" + year + "' AND isActive = 1 AND syncId is NULL AND inTypeId = " + inTypeId + " AND cmpId = " + cmpId + ") group by strftime('%m-%Y', inDate)", null);
        //cursor.moveToFirst();
//        while (!cursor.isAfterLast())
//        {
//            inPaid.put(cursor.getString(cursor.getColumnIndex("monthYear")), cursor.getDouble(cursor.getColumnIndex("inPaid")));
//            inDue.put(cursor.getString(cursor.getColumnIndex("monthYear")), cursor.getDouble(cursor.getColumnIndex("inDue")));
//            cursor.moveToNext();
//        }
        for (int i = 1; i <= 12; i++) {
            inPaid.put("monthYear", (double) (i * 12));
            inDue.put("monthYear", (double) (i * 5));
        }


        ArrayList<Double> inPaidMonthlyDouble = new ArrayList<>(), inDueMonthlyDouble = new ArrayList<>();//to display on the label shown on the bar click
        // this entry is used for bar chart marker on click pole and the value format is double.(paid)
        inPaidMonthlyDouble.add(inPaid.get("01-" + year) != null ? inPaid.get("01-" + year) : 10.0);
        inPaidMonthlyDouble.add(inPaid.get("02-" + year) != null ? inPaid.get("02-" + year) : 526.41);
        inPaidMonthlyDouble.add(inPaid.get("03-" + year) != null ? inPaid.get("03-" + year) : 45.4);
        inPaidMonthlyDouble.add(inPaid.get("04-" + year) != null ? inPaid.get("04-" + year) : 784.4);
        inPaidMonthlyDouble.add(inPaid.get("05-" + year) != null ? inPaid.get("05-" + year) : 624.0);
        inPaidMonthlyDouble.add(inPaid.get("06-" + year) != null ? inPaid.get("06-" + year) : 845.0);
        inPaidMonthlyDouble.add(inPaid.get("07-" + year) != null ? inPaid.get("07-" + year) : 451.74);
        inPaidMonthlyDouble.add(inPaid.get("08-" + year) != null ? inPaid.get("08-" + year) : 745.0);
        inPaidMonthlyDouble.add(inPaid.get("09-" + year) != null ? inPaid.get("09-" + year) : 485.0);
        inPaidMonthlyDouble.add(inPaid.get("10-" + year) != null ? inPaid.get("10-" + year) : 74.0);
        inPaidMonthlyDouble.add(inPaid.get("11-" + year) != null ? inPaid.get("11-" + year) : 785.0);
        inPaidMonthlyDouble.add(inPaid.get("12-" + year) != null ? inPaid.get("12-" + year) : 748.74);

        // this entry is used for bar chart marker on click pole and the value format is double.(due)
        inDueMonthlyDouble.add(inDue.get("01-" + year) != null ? inDue.get("01-" + year) : 74.0);
        inDueMonthlyDouble.add(inDue.get("02-" + year) != null ? inDue.get("02-" + year) : 624.0);
        inDueMonthlyDouble.add(inDue.get("03-" + year) != null ? inDue.get("03-" + year) : 185.0);
        inDueMonthlyDouble.add(inDue.get("04-" + year) != null ? inDue.get("04-" + year) : 851.0);
        inDueMonthlyDouble.add(inDue.get("05-" + year) != null ? inDue.get("05-" + year) : 348.0);
        inDueMonthlyDouble.add(inDue.get("06-" + year) != null ? inDue.get("06-" + year) : 842.0);
        inDueMonthlyDouble.add(inDue.get("07-" + year) != null ? inDue.get("07-" + year) : 474.0);
        inDueMonthlyDouble.add(inDue.get("08-" + year) != null ? inDue.get("08-" + year) : 748.0);
        inDueMonthlyDouble.add(inDue.get("09-" + year) != null ? inDue.get("09-" + year) : 625.0);
        inDueMonthlyDouble.add(inDue.get("10-" + year) != null ? inDue.get("10-" + year) : 952.0);
        inDueMonthlyDouble.add(inDue.get("11-" + year) != null ? inDue.get("11-" + year) : 415.0);
        inDueMonthlyDouble.add(inDue.get("12-" + year) != null ? inDue.get("12-" + year) : 444.0);

        ArrayList<BarEntry> inPaidMonthlyFloat = new ArrayList<>(), inDueMonthlyFloat = new ArrayList<>();// to assign to the chat for plotting
        //bar chart date entry (paid)
        inPaidMonthlyFloat.add(new BarEntry(0, inPaid.get("01-" + year) != null ? inPaid.get("01-" + year).floatValue() : 541.0f));
        inPaidMonthlyFloat.add(new BarEntry(1, inPaid.get("02-" + year) != null ? inPaid.get("02-" + year).floatValue() : 415.0f));
        inPaidMonthlyFloat.add(new BarEntry(2, inPaid.get("03-" + year) != null ? inPaid.get("03-" + year).floatValue() : 1020.0f));
        inPaidMonthlyFloat.add(new BarEntry(3, inPaid.get("04-" + year) != null ? inPaid.get("04-" + year).floatValue() : 2000.0f));
        inPaidMonthlyFloat.add(new BarEntry(4, inPaid.get("05-" + year) != null ? inPaid.get("05-" + year).floatValue() : 954.0f));
        inPaidMonthlyFloat.add(new BarEntry(5, inPaid.get("06-" + year) != null ? inPaid.get("06-" + year).floatValue() : 8475.0f));
        inPaidMonthlyFloat.add(new BarEntry(6, inPaid.get("07-" + year) != null ? inPaid.get("07-" + year).floatValue() : 8958.0f));
        inPaidMonthlyFloat.add(new BarEntry(7, inPaid.get("08-" + year) != null ? inPaid.get("08-" + year).floatValue() : 8748.0f));
        inPaidMonthlyFloat.add(new BarEntry(8, inPaid.get("09-" + year) != null ? inPaid.get("09-" + year).floatValue() : 7814.0f));
        inPaidMonthlyFloat.add(new BarEntry(9, inPaid.get("10-" + year) != null ? inPaid.get("10-" + year).floatValue() : 8748.0f));
        inPaidMonthlyFloat.add(new BarEntry(10, inPaid.get("11-" + year) != null ? inPaid.get("11-" + year).floatValue() : 845.0f));
        inPaidMonthlyFloat.add(new BarEntry(11, inPaid.get("12-" + year) != null ? inPaid.get("12-" + year).floatValue() : 10000.0f));

        //bar chart date entry (Unpaid)
        inDueMonthlyFloat.add(new BarEntry(0, inDue.get("01-" + year) != null ? inDue.get("01-" + year).floatValue() : 8000.0f));
        inDueMonthlyFloat.add(new BarEntry(1, inDue.get("02-" + year) != null ? inDue.get("02-" + year).floatValue() : 8000.0f));
        inDueMonthlyFloat.add(new BarEntry(2, inDue.get("03-" + year) != null ? inDue.get("03-" + year).floatValue() : 8000.0f));
        inDueMonthlyFloat.add(new BarEntry(3, inDue.get("04-" + year) != null ? inDue.get("04-" + year).floatValue() : 10000.0f));
        inDueMonthlyFloat.add(new BarEntry(4, inDue.get("05-" + year) != null ? inDue.get("05-" + year).floatValue() : 10000.0f));
        inDueMonthlyFloat.add(new BarEntry(5, inDue.get("06-" + year) != null ? inDue.get("06-" + year).floatValue() : 10000.0f));
        inDueMonthlyFloat.add(new BarEntry(6, inDue.get("07-" + year) != null ? inDue.get("07-" + year).floatValue() : 12000.0f));
        inDueMonthlyFloat.add(new BarEntry(7, inDue.get("08-" + year) != null ? inDue.get("08-" + year).floatValue() : 12000.0f));
        inDueMonthlyFloat.add(new BarEntry(8, inDue.get("09-" + year) != null ? inDue.get("09-" + year).floatValue() : 12000.0f));
        inDueMonthlyFloat.add(new BarEntry(9, inDue.get("10-" + year) != null ? inDue.get("10-" + year).floatValue() : 15000.0f));
        inDueMonthlyFloat.add(new BarEntry(10, inDue.get("11-" + year) != null ? inDue.get("11-" + year).floatValue() : 15000.0f));
        inDueMonthlyFloat.add(new BarEntry(11, inDue.get("12-" + year) != null ? inDue.get("12-" + year).floatValue() : 15000.0f));


        Map<String, Object> map = new HashMap<>();
        map.put("inPaidMonthlyDouble", inPaidMonthlyDouble);
        map.put("inDueMonthlyDouble", inDueMonthlyDouble);
        map.put("inPaidMonthlyFloat", inPaidMonthlyFloat);
        map.put("inDueMonthlyFloat", inDueMonthlyFloat);

        return map;
    }
}
