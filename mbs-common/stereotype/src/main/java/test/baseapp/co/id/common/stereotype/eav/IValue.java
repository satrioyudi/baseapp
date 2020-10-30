package test.baseapp.co.id.common.stereotype.eav;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public interface IValue<A extends IAttribute>
{
	public A getAttribute();
	public void setAttribute(A attribute);

	public BigDecimal getDecimalValue();
	public void setDecimalValue(BigDecimal decimalValue);

	public BigInteger getNumericValue();
	public void setNumericValue(BigInteger numericValue);

	public Date getDateValue();
	public void setDateValue(Date dateValue);

	public String getStringValue1();
	public void setStringValue1(String stringValue1);

	public String getStringValue2();
	public void setStringValue2(String stringValue2);
}
