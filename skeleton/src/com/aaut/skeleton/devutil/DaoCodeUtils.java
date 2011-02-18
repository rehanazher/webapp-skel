package com.aaut.skeleton.devutil;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.aaut.skeleton.commons.util.Validators;

/**
 * @author James
 * @version $Revision: 1.0 $, $Date: 2007-8-29 下午04:55:40 $ Lasted update:
 *          2010-9-20
 * 
 */
public class DaoCodeUtils {

	private static final String NEWLINE = "\"\n\t + \"";
	private static final String SQL_HEAD = "private static final String SQL_";

	private static final String ID = "Id";
	private static final String CREATIONTIME = "creationTime";

	private String tableName;
	private String entityName;

	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<Integer> dataTypes = new ArrayList<Integer>();

	boolean hasId = false;
	boolean hasCreationTime = false;

	public DaoCodeUtils(String tableName, String entityName) {
		this.tableName = tableName;
		this.entityName = entityName;

		List<String> configFileNames = new ArrayList<String>();
		// configuration directory or file
		File file = new File("/src/config/");
		if (file.isDirectory()) {
			if (file.getName().endsWith(".xml"));
			configFileNames.add(file.getAbsolutePath());
		} else {
			File[] subFiles = file.listFiles();
			for (File sub : subFiles) {
				if (sub.isFile() && sub.getName().endsWith(".xml")) {
					configFileNames.add(file.getAbsolutePath());
				}
			}
		}

		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(
				configFileNames.toArray(new String[configFileNames.size()]));

		DataSource dataSource = (DataSource) ctx.getBean("c3p0DS");

		try {
			Connection conn = dataSource.getConnection();
			// DatabaseMetaData dmd = conn.getMetaData();

			// ResultSet rs = conn.getMetaData().getColumns(null,
			// dmd.getUserName(), tableName, "%");

			ResultSet rs = conn.getMetaData().getColumns(null, null, tableName,
					"%");

			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME");
				int dataType = rs.getInt("DATA_TYPE");

				names.add(name);
				dataTypes.add(new Integer(dataType));

				if (ID.equalsIgnoreCase(name)) {
					hasId = true;
				}
				if (CREATIONTIME.equalsIgnoreCase(name)) {
					hasCreationTime = true;
				}
			}

			rs.close();
			conn.close();

			if (!hasId) {
				System.out
						.println("Table[" + tableName + "] has no column[ID]");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getJavaType(int dataType) {
		switch (dataType) {
		case Types.INTEGER:
		case Types.TINYINT:
		case Types.BIGINT:
			return "int";
		case Types.NUMERIC:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
			return "double";
		case Types.TIMESTAMP:
		case Types.DATE:
		case Types.TIME:
			return "Date";
		default:
			return "String";
		}

	}

	private String getJavaMethod(int dataType) {
		if (dataType == Types.INTEGER) {
			return "getInt";
		} else if (dataType == Types.TIMESTAMP || dataType == Types.DATE
				|| dataType == Types.TIME) {
			return "getTimestamp";
		} else {
			return "getString";
		}
	}

	private String getSQLType(int dataType) {
		switch (dataType) {
		case Types.CHAR:
			return "Types.CHAR";
		case Types.VARCHAR:
			return "Types.VARCHAR";
		case Types.INTEGER:
			return "Types.INTEGER";
		case Types.TINYINT:
			return "Types.TINYINT";
		case Types.BIGINT:
			return "Types.BIGINT";
		case Types.NUMERIC:
			return "Types.NUMERIC";
		case Types.DECIMAL:
			return "Types.DECIMAL";
		case Types.DOUBLE:
			return "Types.DOUBLE";
		case Types.FLOAT:
			return "Types.FLOAT";
		case Types.TIMESTAMP:
			return "Types.TIMESTAMP";
		case Types.DATE:
			return "Types.DATE";
		case Types.TIME:
			return "Types.TIME";
		default:
			return "UNDEFINED";
		}

	}

	private String getQuestionMark(int size) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append("?");
		}
		return sb.toString();
	}

	private String initialToUpperCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private String initialToLowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	private String getSetMethod(String name) {
		StringBuffer sb = new StringBuffer();
		sb.append(initialToLowerCase(entityName));
		sb.append(".set");
		sb.append(initialToUpperCase(name));
		return sb.toString();
	}

	private String getGetMethod(String name) {
		StringBuffer sb = new StringBuffer();
		sb.append(initialToLowerCase(entityName));
		sb.append(".get");
		sb.append(initialToUpperCase(name));
		sb.append("()");
		return sb.toString();
	}

	public String getEntityCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);

			name = generateJavaPropertyName(name);

			int dataType = dataTypes.get(i);

			sb.append("private ");
			sb.append(getJavaType(dataType));
			sb.append(" ");
			sb.append(name);
			sb.append(";\n");
		}
		return sb.toString();
	}

	private String generateJavaPropertyName(String fieldName) {
		if (Validators.isEmpty(fieldName)) {
			return fieldName;
		}

		String propertyName = fieldName.toLowerCase();

		StringBuffer propertyNameBuffer = new StringBuffer();
		for (int i = 0; i < propertyName.length(); i++) {
			char c = propertyName.charAt(i);
			char nc = (i < propertyName.length() - 1) ? propertyName
					.charAt(i + 1) : (char) -1;

			if (c == '_' && nc >= 'a' && nc <= 'z') {
				propertyNameBuffer.append(String.valueOf(nc).toUpperCase());
				i++;
			} else if (c != '_') {
				propertyNameBuffer.append(c);
			}
		}

		propertyName = propertyNameBuffer.toString();

		return propertyName;
	}

	public String getRowMapping() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);

			int dataType = dataTypes.get(i);

			sb.append(getSetMethod(generateJavaPropertyName(name)));
			sb.append("(rs.");
			sb.append(getJavaMethod(dataType));

			sb.append("(\"");
			sb.append(name);
			sb.append("\"));\n");
		}

		return sb.toString();
	}

	public String getFindAllSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append(SQL_HEAD);
		sb.append("FIND_ALL");
		sb.append(" = \"SELECT * FROM ");
		sb.append(tableName);
		sb.append("\";\n");

		return sb.toString();
	}

	public String getAddSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append(SQL_HEAD);
		sb.append("INSERT_");
		sb.append(entityName.toUpperCase());
		sb.append(" = \"INSERT INTO ");
		sb.append(tableName);
		sb.append("(");

		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			// int dataType = dataTypes.get(i);

			if (i > 0) {
				sb.append(",");
			}
			if (i == 3 || i == 10 || i == 17) {
				sb.append(NEWLINE);
			}
			sb.append(name);
		}

		sb.append(") ");
		sb.append(NEWLINE);
		sb.append("VALUES(");
		sb.append(getQuestionMark(names.size()));
		sb.append(")\";\n");

		return sb.toString();
	}

	public String getRemoveSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append(SQL_HEAD);
		sb.append("DELETE_");
		sb.append(entityName.toUpperCase());
		sb.append(" = \"DELETE FROM ");
		sb.append(tableName);
		sb.append(" WHERE id=?\";\n");
		return sb.toString();
	}

	public String getModifySQL() {
		StringBuffer sb = new StringBuffer();
		sb.append(SQL_HEAD);
		sb.append("UPDATE_");
		sb.append(entityName.toUpperCase());
		sb.append(" = \"UPDATE ");
		sb.append(tableName);
		sb.append(" SET ");

		int index = 0;
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);

			if (ID.equalsIgnoreCase(name)
					|| CREATIONTIME.equalsIgnoreCase(name)) {
				continue;
			}

			if (index > 0) {
				sb.append(",");
			}

			if (index == 2 || index == 9 || index == 16) {
				sb.append(NEWLINE);
			}
			sb.append(name);
			sb.append("=?");

			index++;
		}

		sb.append(" WHERE id=?\";\n");

		return sb.toString();
	}

	public String getFindByIdSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append(SQL_HEAD);
		sb.append("FIND_");
		sb.append(entityName.toUpperCase());
		sb.append("_BY_ID");
		sb.append(" = \"SELECT * FROM ");
		sb.append(tableName);
		sb.append(" WHERE id=?\";\n");
		return sb.toString();
	}

	public String getFindByIdsSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append(SQL_HEAD);
		sb.append("FIND_");
		sb.append(entityName.toUpperCase());
		sb.append("S_BY_IDS");
		sb.append(" = \"SELECT * FROM ");
		sb.append(tableName);
		sb.append(" WHERE id IN\";\n");
		return sb.toString();
	}

	public String getFindAllMethod() {
		StringBuffer sb = new StringBuffer();
		sb.append("public List<");
		sb.append(entityName);
		sb.append("> findAll");
		sb.append("(");
		sb.append(") {\n\t");
		sb.append("return query(SQL_FIND_ALL, new ");
		sb.append(entityName);
		sb.append("MultiRowMapper());\n");
		sb.append("}\n\t");
		return sb.toString();
	}

	public String getAddMethod() {
		StringBuffer sb = new StringBuffer();
		sb.append("public String insert");
		sb.append("(");
		sb.append(entityName);
		sb.append(" ");
		sb.append(initialToLowerCase(entityName));
		sb.append(") {\n\t");

		sb.append(getSetMethod("id"));
		sb.append("(createId());\n\t");
		// sb.append(getSetMethod("creationtime"));
		// sb.append("(new Date());\n\t");

		sb.append("if (");
		sb.append("update(SQL_INSERT_");
		sb.append(entityName.toUpperCase());
		sb.append(", new Object[] {");

		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);

			name = generateJavaPropertyName(name);

			int dataType = dataTypes.get(i);

			if (i > 0) {
				sb.append(",");
			}

			if (dataType == Types.INTEGER) {
				sb.append("new Integer(");
				sb.append(getGetMethod(name));
				sb.append(")");
			} else {
				sb.append(getGetMethod(name));
			}
		}

		sb.append("}, new int[] {");

		for (int i = 0; i < names.size(); i++) {
			// String name = (String) names.get(i);
			int dataType = dataTypes.get(i);

			if (i > 0) {
				sb.append(",");
			}

			sb.append(getSQLType(dataType));
		}

		sb.append("}) ");
		sb.append(" > 0){\n");
		sb.append("return ");
		sb.append(initialToLowerCase(entityName));
		sb.append(".getId();\n");
		sb.append("}");
		sb.append("else{");
		sb.append("return null;");
		sb.append("}");
		sb.append("}\n");

		return sb.toString();
	}

	/**
	 * ���delete�ķ�������
	 * 
	 * @return
	 */
	public String getRemoveMethod() {
		StringBuffer sb = new StringBuffer();
		sb.append("public String delete");
		sb.append("(String ");
		sb.append(initialToLowerCase(entityName));
		sb.append("Id");
		sb.append(") {\n\t");

		sb.append("if (");
		sb.append("update(SQL_DELETE_");
		sb.append(entityName.toUpperCase());
		sb.append(",");
		sb.append(initialToLowerCase(entityName));
		sb.append("Id)");
		sb.append(" > 0){\n");
		sb.append("return ");
		sb.append(initialToLowerCase(entityName));
		sb.append("Id;");
		;
		sb.append("}");
		sb.append("else{");
		sb.append("return null;");
		sb.append("}");
		sb.append("}\n");

		return sb.toString();
	}

	public String getModifyMethod() {
		StringBuffer sb = new StringBuffer();
		sb.append("public String update");
		sb.append("(");
		sb.append(entityName);
		sb.append(" ");
		sb.append(initialToLowerCase(entityName));
		sb.append(") {\n\t");

		sb.append("if (");
		sb.append("update(SQL_UPDATE_");
		sb.append(entityName.toUpperCase());
		sb.append(", new Object[] {");

		int idIndex = -1;
		int creationTimeIndex = -1;

		int index = 0;
		for (int i = 0; i < names.size(); i++) {
			String name = (String) names.get(i);
			int dataType = ((Integer) dataTypes.get(i)).intValue();

			if (ID.equalsIgnoreCase(name)) {
				idIndex = i;
				continue;
			}

			if (CREATIONTIME.equalsIgnoreCase(name)) {
				creationTimeIndex = i;
				continue;
			}

			if (index > 0) {
				sb.append(",");
			}

			name = generateJavaPropertyName(name);

			if (dataType == Types.INTEGER) {
				sb.append("new Integer(");
				sb.append(getGetMethod(name));
				sb.append(")");
			} else {
				sb.append(getGetMethod(name));
			}

			index++;
		}

		sb.append(",");
		sb.append(getGetMethod(ID));
		sb.append("}, new int[] {");

		index = 0;
		for (int i = 0; i < names.size(); i++) {
			// String name = (String) names.get(i);
			int dataType = dataTypes.get(i);

			if (i == idIndex || i == creationTimeIndex) {
				continue;
			}

			if (index > 0) {
				sb.append(",");
			}

			sb.append(getSQLType(dataType));

			index++;
		}

		sb.append(", Types.CHAR");
		sb.append("}) ");
		sb.append(" > 0){\n");
		sb.append("return ");
		sb.append(initialToLowerCase(entityName));
		sb.append(".getId();\n");
		sb.append("}");
		sb.append("else{");
		sb.append("return null;");
		sb.append("}");
		sb.append("}\n");

		return sb.toString();
	}

	public String getFindByIdMethod() {
		StringBuffer sb = new StringBuffer();
		sb.append("public ");
		sb.append(entityName);
		sb.append(" find");
		sb.append("ById(String ");
		sb.append(initialToLowerCase(entityName));
		sb.append("Id");
		sb.append(") {\n\t");

		sb.append("return (");
		sb.append(entityName);
		sb.append(") query(SQL_FIND_");
		sb.append(entityName.toUpperCase());
		sb.append("_BY_ID, ");
		sb.append(initialToLowerCase(entityName));
		sb.append("Id, new ");
		sb.append(entityName);
		sb.append("SingleRowMapper());\n");
		sb.append("}\n");

		return sb.toString();
	}

	public String getFindByIdsMethod() {
		StringBuffer sb = new StringBuffer();
		sb.append("public List<");
		sb.append(entityName);
		sb.append("> find");
		sb.append("ByIds(String[] ");
		sb.append(initialToLowerCase(entityName));
		sb.append("Ids");
		sb.append(") {\n\t");

		sb.append("return (");
		sb.append(entityName);
		sb.append(") queryForInSQL(SQL_FIND_");
		sb.append(entityName.toUpperCase());
		sb.append("S_BY_IDS, null, ");
		sb.append(initialToLowerCase(entityName));
		sb.append("Ids, new ");
		sb.append(entityName);
		sb.append("MultiRowMapper());\n");
		sb.append("}\n");

		return sb.toString();
	}

	public void printCode() {
		System.out.println(getEntityCode());
		System.out.println(getRowMapping());

		System.out.println(getFindAllSQL());
		System.out.println(getAddSQL());
		System.out.println(getRemoveSQL());
		System.out.println(getModifySQL());
		System.out.println(getFindByIdSQL());
		System.out.println(getFindByIdsSQL());

		System.out.println(getFindAllMethod());
		System.out.println(getAddMethod());
		System.out.println(getRemoveMethod());
		System.out.println(getModifyMethod());
		System.out.println(getFindByIdMethod());
		System.out.println(getFindByIdsMethod());
	}
	
	private File

	public static void main(String[] args) {
		DaoCodeUtils builder = new DaoCodeUtils("CCP_COUNTRIES", "Country");
		builder.printCode();
	}

}
