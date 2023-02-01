import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.parser.Entity;


/**
 * Tests creating an index and using EXPLAIN on a MySQL database.
 */
public class IndexMySQL 
{
	/**
	 * Connection to database
	 */
	private Connection con;
	
	
	/**
	 * Main method is only used for convenience.  Use JUnit test file to verify your answer.
	 * 
	 * @param args
	 * 		none expected
	 * @throws SQLException
	 * 		if a database error occurs
	 */
	public static void main(String[] args) throws SQLException
	{
		IndexMySQL q = new IndexMySQL();
		q.connect();	
		q.drop();
		q.create();
		q.insert(10000);	
		q.addindex1();	
		q.addindex2();			
		q.close();
	}

	/**
	 * Makes a connection to the database and returns connection to caller.
	 * 
	 * @return
	 * 		connection
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public Connection connect() throws SQLException
	{
		String url = "jdbc:mysql://localhost/lab2";
		String uid = "testuser";
		String pw = "404testpw";
		
		System.out.println("Connecting to database.");
		// Note: Must assign connection to instance variable as well as returning it back to the caller
		// TODO: Make a connection to the database and store connection in con variable before returning it.
		con = DriverManager.getConnection(url, uid, pw);  
		return con;	  	                       
	}
	
	/**
	 * Closes connection to database.
	 */
	public void close()
	{
		System.out.println("Closing database connection.");
		// TODO: Close the database connection.  Catch any exception and print out if it occurs.
		if(con != null){
			try{
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Drops the table from the database.  If table does not exist, error is ignored.
	 */
	public void drop()
	{
		System.out.println("Dropping table bench.");
		// TODO: Drop the table bench.  Catch any exception and print out if it occurs.	
		try {		      
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS bench";
			stmt.executeUpdate(sql);
			System.out.println("Table is deleted.");   	  
		} catch (SQLException e) {
			System.out.println("Table doesn't exist.");
		} 
	}
	
	/**
	 * Creates the table in the database.  Table name: bench
	 * Fields:
	 *  - id - integer, must auto-increment
	 *  - val1 - integer (starts at 1 and each record increases by 1)
	 *  - val2 - integer (val1 % 10)
	 *  - str1 - varchar(20) = "Test"+val1
	 * Note: You do NOT need to use calculated or derived fields.
	 */
	public void create() throws SQLException
	{
		System.out.println("Creating table bench.");
		// TODO: Create the table bench.
		try{
			Statement stmt = con.createStatement();
			String sql_create = "CREATE TABLE bench(" + "id INT," + "val1 INT," + "val2 INT," + "str1 VARCHAR(20)" + ")";
			stmt.executeUpdate(sql_create);

			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Inserts the test records in the database.  Must used a PreparedStatement.  
	 * 
	 * Challenge: There are ways to speed this up. Bonus +1 mark for finding a technique.	 
	 */
	public void insert(int numRecords) throws SQLException
	{
		System.out.println("Inserting records.");		
		// TODO: Insert records		
		
		try{

			String sql_insert = "INSERT INTO bench(" + "id," + "val1," + "val2," + "str1," + ") VALUES(" + "?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(sql_insert);

			for(int i = 1; i < numRecords+1; i++){
				pstmt.setInt(1, i);
				pstmt.setInt(2, i+1);
				pstmt.setInt(3, (i+1) % 10);
				pstmt.setString(4, "Test" + (i+1));
				pstmt.addBatch();
			}
			pstmt.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a unique index on val1 for bench table.  Returns result of explain.
	 * 
	 * @return
	 * 		ResultSet
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public ResultSet addindex1() throws SQLException
	{
		System.out.println("Building index #1.");
		// TODO: Create index
		
		// TODO: Do explain with query: SELECT * FROM bench WHERE val1 = 500
		return null;	
	}
	
	/**
	 * Creates an index on val2 and val1 for bench table.  Returns result of explain.
	 * 
	 * @return
	 * 		ResultSet
	 * @throws SQLException
	 * 		if an error occurs
	 */
	public ResultSet addindex2() throws SQLException
	{
		System.out.println("Building index #2.");
		// TODO: Create index
		
		// TODO: Do explain with query: SELECT * FROM bench WHERE val2 = 0 and val1 > 100;
		return null;	
	}
	
	
	/*
	 * Do not change anything below here.
	 */
	/**
     * Converts a ResultSet to a string with a given number of rows displayed.
     * Total rows are determined but only the first few are put into a string.
     * 
     * @param rst
     * 		ResultSet
     * @param maxrows
     * 		maximum number of rows to display
     * @return
     * 		String form of results
     * @throws SQLException
     * 		if a database error occurs
     */    
    public static String resultSetToString(ResultSet rst, int maxrows) throws SQLException
    {                       
        StringBuffer buf = new StringBuffer(5000);
        int rowCount = 0;
        ResultSetMetaData meta = rst.getMetaData();
        buf.append("Total columns: " + meta.getColumnCount());
        buf.append('\n');
        if (meta.getColumnCount() > 0)
            buf.append(meta.getColumnName(1));
        for (int j = 2; j <= meta.getColumnCount(); j++)
            buf.append(", " + meta.getColumnName(j));
        buf.append('\n');
                
        while (rst.next()) 
        {
            if (rowCount < maxrows)
            {
                for (int j = 0; j < meta.getColumnCount(); j++) 
                { 
                	Object obj = rst.getObject(j + 1);                	 	                       	                                	
                	buf.append(obj);                    
                    if (j != meta.getColumnCount() - 1)
                        buf.append(", ");                    
                }
                buf.append('\n');
            }
            rowCount++;
        }            
        buf.append("Total results: " + rowCount);
        return buf.toString();
    }
    
    /**
     * Converts ResultSetMetaData into a string.
     * 
     * @param meta
     * 		 ResultSetMetaData
     * @return
     * 		string form of metadata
     * @throws SQLException
     * 		if a database error occurs
     */
    public static String resultSetMetaDataToString(ResultSetMetaData meta) throws SQLException
    {
	    StringBuffer buf = new StringBuffer(5000);                                   
	    buf.append(meta.getColumnName(1)+" ("+meta.getColumnLabel(1)+", "+meta.getColumnType(1)+"-"+meta.getColumnTypeName(1)+", "+meta.getColumnDisplaySize(1)+", "+meta.getPrecision(1)+", "+meta.getScale(1)+")");
	    for (int j = 2; j <= meta.getColumnCount(); j++)
	        buf.append(", "+meta.getColumnName(j)+" ("+meta.getColumnLabel(j)+", "+meta.getColumnType(j)+"-"+meta.getColumnTypeName(j)+", "+meta.getColumnDisplaySize(j)+", "+meta.getPrecision(j)+", "+meta.getScale(j)+")");
	    return buf.toString();
    }
}
