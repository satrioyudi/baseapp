package test.baseapp.co.id.common.stereotype.flowable;

import java.util.Set;

public interface IHistoricalFlow<S, H extends IFlowHistory<?, S>> extends IFlow<S>
{
	public Set<H> getHistories();
	public void setHistories(Set<H> histories);
	
	public H addNewHistory();
}