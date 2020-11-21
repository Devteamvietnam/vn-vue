package com.devteam.quartz.util;

import org.quartz.JobExecutionContext;
import com.devteam.quartz.domain.SysJob;

/**
 * Timing task processing (allowing concurrent execution)
 *
 * @author ivan
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
