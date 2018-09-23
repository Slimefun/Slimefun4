package me.mrCookieSlime.Slimefun.MySQL.Components;

import me.mrCookieSlime.Slimefun.MySQL.MySQLMain;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.util.List;

public enum DataTypeEnum {
    /*
INT("INT"),
TINYINT("TINYINT"),
SMALLINT("SMALLINT"),
MEDIUMINT("MEDIUMINT"),
BIGINT("BIGINT"),
FLOAT("FLOAT"),
DOUBLE("DOUBLE"),
DATE("DATE"),
DATETIME("DATETIME"),
TIMESTAMP("TIMESTAMP"),
TIME("TIME"),
YEAR("YEAR"),
CHAR("CHAR"),
TEXT("TEXT"),
TINYTEXT("TINYTEXT"),
MEDIUMTEXT("MEDIUMTEXT"),
LONGTEXT("LONGTEXT");*/

    BYTE("byte"),
    SHORT("short"),
    INTEGER("integer"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    STRING("string"),
    CHARARRAY("chararray"),
    BOOLEAN("boolean"),
    ITEMSTACK("itemstack"),
    LOCATION("location"),
    UUID("uuid"),
    INTLIST("intlist"),
    STRINGLIST("stringlist"),
    ITEMLIST("itemlist");

    private final String name;

    DataTypeEnum(String name) {
        this.name = name;
    }

    public static DataTypeEnum getObject(Object value)
    {

                if (value instanceof Byte)
                {
                    return DataTypeEnum.BYTE;
                }
                if (value instanceof Short)
                {
                    return DataTypeEnum.SHORT;
                }
                if (value instanceof Integer)
                {
                    return DataTypeEnum.INTEGER;
                }
                if (value instanceof Long)
                {
                    return DataTypeEnum.LONG;
                }
                if (value instanceof Float)
                {
                    return DataTypeEnum.FLOAT;
                }
                if (value instanceof Double)
                {
                    return DataTypeEnum.DOUBLE;
                }
                if (value instanceof String)
                {
                    return DataTypeEnum.STRING;
                }
                if (value instanceof Boolean)
                {
                    return DataTypeEnum.BOOLEAN;
                }
                if (value instanceof ItemStack)
                {
                    return DataTypeEnum.ITEMSTACK;
                }
                if (value instanceof Location)
                {
                    return DataTypeEnum.LOCATION;
                }
                if (value instanceof java.util.UUID)
                {
                    return DataTypeEnum.UUID;
                }
                if (value instanceof List)
                {
                    if (((List)value).size() > 0)
                    {
                        if (((List) value).get(0) instanceof Integer) {
                            return DataTypeEnum.INTLIST;
                        }
                    }
                }
                if (value instanceof List)
                {
                    if (((List)value).size() > 0)
                    {
                        if (((List) value).get(0) instanceof String) {
                            return DataTypeEnum.STRINGLIST;
                        }
                    }
                }
                if (value instanceof List)
                {
                    if (((List)value).size() > 0)
                    {
                        if (((List) value).get(0) instanceof ItemStack) {
                            return DataTypeEnum.ITEMLIST;
                        }
                    }
                }
                if (value instanceof String)
                {
                    return DataTypeEnum.CHARARRAY;
                }
        return null;
    }
    public boolean checkObject(Object value)
    {
        switch (this.name)
        {
            case "byte":
                if (value instanceof Byte)
                {
                    return true;
                }
                return false;
            case "short":
                if (value instanceof Short)
                {
                    return true;
                }
                return false;
            case "integer":
                if (value instanceof Integer)
                {
                    return true;
                }
                return false;
            case "long":
                if (value instanceof Long)
                {
                    return true;
                }
                return false;
            case "float":
                if (value instanceof Float)
                {
                    return true;
                }
                return false;
            case "double":
                if (value instanceof Double)
                {
                    return true;
                }
                return false;
            case "string":
                if (value instanceof String)
                {
                    return true;
                }
                return false;
            case "boolean":
                if (value instanceof Boolean)
                {
                    return true;
                }
                return false;
            case "itemstack":
                if (value instanceof ItemStack)
                {
                    return true;
                }
                return false;
            case "location":
                if (value instanceof Location)
                {
                    return true;
                }
                return false;
            case "uuid":
                if (value instanceof java.util.UUID)
                {
                    return true;
                }
                return false;
            case "intlist":
                if (value instanceof List)
                {
                    if (((List)value).size() > 0)
                    {
                        if (((List) value).get(0) instanceof Integer) {
                            return true;
                        }
                    }
                }
                return false;
            case "stringlist":
                if (value instanceof List)
                {
                    if (((List)value).size() > 0)
                    {
                        if (((List) value).get(0) instanceof String) {
                            return true;
                        }
                    }
                }
                return false;
            case "itemlist":
                if (value instanceof List)
                {
                    if (((List)value).size() > 0)
                    {
                        if (((List) value).get(0) instanceof ItemStack) {
                            return true;
                        }
                    }
                }
                return false;
            case "chararray":
                if (value instanceof String)
                {
                    return true;
                }
                return false;

        }
        return false;
    }
    public void setPreparedStatement(PreparedStatement ps, Integer index, Object value)
    {
        try {
            switch (this.name)
            {
                case "byte":
                    ps.setByte(index, (Byte) value);
                    break;
                case "short":
                    ps.setShort(index, (Short) value);
                    break;
                case "integer":
                    ps.setInt(index,(int) value);
                    break;
                case "long":
                    ps.setLong(index,(long) value);
                    break;
                case "float":
                    ps.setFloat(index, (float) value);
                    break;
                case "double":
                    ps.setDouble(index, (double) value);
                    break;
                case "string":
                    ps.setString(index, (String) value);
                    break;
                case "boolean":
                    ps.setBoolean(index, (Boolean) value);
                    break;
                case "itemstack":
                    ps.setString(index, MySQLMain.encode((ItemStack)value));
                    break;
                case "location":
                    ps.setString(index, MySQLMain.encode((Location)value));
                    break;
                case "uuid":
                    ps.setString(index, ((java.util.UUID)value).toString());
                    break;
                case "intlist":
                    ps.setString(index, MySQLMain.encode((List<Integer>)value));
                    break;
                case "stringlist":
                    ps.setString(index, MySQLMain.encode((List<String>)value));
                    break;
                case "itemlist":
                    ps.setString(index, MySQLMain.encode((List<ItemStack>)value));
                    break;
                case "chararray":
                    ps.setString(index, (String) value);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }


}