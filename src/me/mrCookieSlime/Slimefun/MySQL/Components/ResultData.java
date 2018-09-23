package me.mrCookieSlime.Slimefun.MySQL.Components;

import me.mrCookieSlime.Slimefun.MySQL.MySQLMain;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ResultData {
    private DataType dataType;
    private Object result;
    public ResultData(DataType DataType, Object result)
    {
        this.dataType = DataType;
        this.result = result;
    }
    public DataType getDataType()
    {
        return dataType;
    }
    public String getName()
    {
        return dataType.getName();
    }
    public Byte getByte()
    {
        if (dataType.getType() == DataTypeEnum.BYTE)
        {
            try
            {
                return (Byte) result;
            }
            catch (Exception e)
            {
                try {
                    return Byte.valueOf(result + "");
                }
                catch (Exception e2)
                {
                    return null;
                }
            }
        }
        return null;
    }
    public Short getShort()
    {
        if (dataType.getType() == DataTypeEnum.SHORT)
        {
            try
            {
                return (Short) result;
            }
            catch (Exception e)
            {
                try {
                    return Short.valueOf(result + "");
                }
                catch (Exception e2)
                {
                    return null;
                }
            }
        }
        return null;
    }
    public Integer getInteger()
    {
        if (dataType.getType() == DataTypeEnum.INTEGER)
        {
            try
            {
                return (Integer) result;
            }
            catch (Exception e)
            {
                try {
                    return Integer.valueOf(result + "");
                }
                catch (Exception e2)
                {
                    return null;
                }
            }
        }
        return null;
    }
    public Long getLong()
    {
        if (dataType.getType() == DataTypeEnum.LONG)
        {
            try
            {
                return (Long) result;
            }
            catch (Exception e)
            {
                try {
                    return Long.valueOf(result + "");
                }
                catch (Exception e2)
                {
                    return null;
                }
            }
        }
        return null;
    }
    public Float getFloat()
    {
        if (dataType.getType() == DataTypeEnum.FLOAT)
        {
            try
            {
                return (Float) result;
            }
            catch (Exception e)
            {
                try {
                    return Float.valueOf(result + "");
                }
                catch (Exception e2)
                {
                    return null;
                }
            }
        }
        return null;
    }
    public Double getDouble()
    {
        if (dataType.getType() == DataTypeEnum.DOUBLE)
        {
            try
            {
                return (Double) result;
            }
            catch (Exception e)
            {
                try {
                    return Double.valueOf(result + "");
                }
                catch (Exception e2)
                {
                    return null;
                }
            }
        }
        return null;
    }
    public String getString()
    {
        if ((dataType.getType() == DataTypeEnum.STRING) || (dataType.getType() == DataTypeEnum.CHARARRAY))
        {
            try
            {
                return (String) result;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }
    public Boolean getBoolean()
    {
        if (dataType.getType() == DataTypeEnum.BOOLEAN)
        {
            try
            {
                return (Boolean) result;
            }
            catch (Exception e)
            {
                try {
                    return Boolean.valueOf(result + "");
                }
                catch (Exception e2)
                {
                    return null;
                }
            }
        }
        return null;
    }
    public ItemStack getItemStack()
    {
        if (dataType.getType() == DataTypeEnum.ITEMSTACK)
        {
            try
            {
                ItemStack nresults = MySQLMain.decodeItemStack((String)result);
                return nresults;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }
    public Location getLocation()
    {
        if (dataType.getType() == DataTypeEnum.LOCATION)
        {
            try
            {
                Location nresults = MySQLMain.decodeLocation((String)result);
                return nresults;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }
    public UUID getUUID()
    {
        if (dataType.getType() == DataTypeEnum.UUID)
        {
            try
            {
                return UUID.fromString((String)result);
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }
    public List<Integer> getIntList()
    {
        if (dataType.getType() == DataTypeEnum.INTLIST)
        {
            try
            {
                List<Integer> nresults = MySQLMain.decodeIntList((String)result);
                return nresults;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }
    public List<String> getStringList()
    {
        if (dataType.getType() == DataTypeEnum.STRINGLIST)
        {
            try
            {
                List<String> nresults = MySQLMain.decodeStringList((String)result);
                return nresults;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }
    public List<ItemStack> getItemList()
    {
        if (dataType.getType() == DataTypeEnum.ITEMLIST)
        {
            try
            {
                List<ItemStack> nresults = MySQLMain.decodeItemList((String)result);
                return nresults;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }
    public Object get()
    {
        try {
            if (dataType.getType() == DataTypeEnum.BYTE) {
                return (Byte) result;
            }
            if (dataType.getType() == DataTypeEnum.SHORT) {
                return (Short) result;
            }
            if (dataType.getType() == DataTypeEnum.INTEGER) {
                return (Integer) result;
            }
            if (dataType.getType() == DataTypeEnum.LONG) {
                return (Long) result;

            }
            if (dataType.getType() == DataTypeEnum.FLOAT) {
                return (Float) result;

            }
            if (dataType.getType() == DataTypeEnum.DOUBLE) {
                return (Double) result;

            }
            if (dataType.getType() == DataTypeEnum.STRING || dataType.getType() == DataTypeEnum.CHARARRAY) {
                return (String) result;

            }
            if (dataType.getType() == DataTypeEnum.BOOLEAN) {
                return (Boolean) result;

            }
            if (dataType.getType() == DataTypeEnum.ITEMSTACK) {
                ItemStack nresults = MySQLMain.decodeItemStack((String) result);
                return nresults;

            }
            if (dataType.getType() == DataTypeEnum.LOCATION) {

                Location nresults = MySQLMain.decodeLocation((String) result);
                return nresults;

            }
            if (dataType.getType() == DataTypeEnum.UUID) {

                return UUID.fromString((String) result);

            }
            if (dataType.getType() == DataTypeEnum.INTLIST) {

                List<Integer> nresults = MySQLMain.decodeIntList((String) result);
                return nresults;

            }
            if (dataType.getType() == DataTypeEnum.INTLIST) {

                List<String> nresults = MySQLMain.decodeStringList((String) result);
                return nresults;

            }
            if (dataType.getType() == DataTypeEnum.ITEMLIST) {

                List<ItemStack> nresults = MySQLMain.decodeItemList((String) result);
                return nresults;

            }
            return result;
        }
        catch (Exception e)
        {
            return result;
        }
    }
}
