package utility;

import org.testng.annotations.DataProvider;

import java.io.IOException;

public class DataProviders {
    @DataProvider(name="all_data")
    public String[][] getAllData() throws IOException {
        String path=System.getProperty("user.dir")+"//datatest//datatesting.xlsx";
        ExcelUtil xl = new ExcelUtil(path);

        int rownum = xl.getRowCount("Sheet1");
        int colcount = xl.getCellCount("Sheet1",1);

        String apidata[][] = new String[rownum][colcount];

        for(int i=1;i<=rownum;i++) {
            for(int j=0;j<colcount;j++) {
                apidata[i-1][j]=xl.getCellData("Sheet1", i, j);
            }
        }
        return apidata;
    }

    @DataProvider(name="by_username")
    public String[] getUsername() throws IOException {
        String path=System.getProperty("user.dir")+"//datatest//datatesting.xlsx";
        ExcelUtil xl = new ExcelUtil(path);

        int rownum=xl.getRowCount("Sheet1");
        String apidata[]=new String[rownum];
        for(int i=1;i<=rownum;i++) {
            apidata[i-1]=xl.getCellData("Sheet1", i, 0);
        }
        return apidata;
    }

    @DataProvider(name="by_user_n_pass")
    public String[][] getUsernameAndPassword() throws IOException {
        String path=System.getProperty("user.dir")+"//datatest//datatesting.xlsx";
        ExcelUtil xl = new ExcelUtil(path);

        int rownum = xl.getRowCount("Sheet1"); // to the max !!
        int colcount = 2; // maksimal 2 kolom (index ke-0 s.d. ke-1)

        String apidata[][] = new String[rownum][2];

        for(int i=1;i<=rownum;i++) { // baris index ke-1 (baris 2) soalnya bagian header kita skip
            for(int j=0;j<colcount;j++) { // kolom index ke-0 (baris 1)
                apidata[i-1][j]=xl.getCellData("Sheet1", i, j);
            }
        }
        return apidata;
    }

    @DataProvider(name="update_all_data")
    public String[][] getUpdateAllData() throws IOException {
        String path=System.getProperty("user.dir")+"//datatest//datatesting.xlsx";
        ExcelUtil xl = new ExcelUtil(path);

        int rownum = xl.getRowCount("ForUpdate");
        int colcount = xl.getCellCount("ForUpdate",1);

        String apidata[][] = new String[rownum][colcount];

        for(int i=1;i<=rownum;i++) {
            for(int j=0;j<colcount;j++) {
                apidata[i-1][j]=xl.getCellData("ForUpdate", i, j);
            }
        }
        return apidata;
    }

}
