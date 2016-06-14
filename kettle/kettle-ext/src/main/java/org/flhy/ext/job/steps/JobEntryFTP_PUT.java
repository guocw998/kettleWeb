package org.flhy.ext.job.steps;

import java.util.List;

import org.flhy.ext.core.PropsUI;
import org.flhy.ext.job.step.AbstractJobEntry;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.entries.ftpput.JobEntryFTPPUT;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.trans.steps.textfileoutput.TextFileOutputMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import bsh.StringUtil;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("FTP_PUT")
@Scope("prototype")
public class JobEntryFTP_PUT extends AbstractJobEntry{
	@Override
	public void decode(JobEntryInterface jobEntry, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		org.pentaho.di.job.entries.ftpput.JobEntryFTPPUT jobEntryFTPPUT = (org.pentaho.di.job.entries.ftpput.JobEntryFTPPUT) jobEntry;
		//一般---服务器设置
		jobEntryFTPPUT.setUserName(cell.getAttribute("username"));
		jobEntryFTPPUT.setServerPort(cell.getAttribute("serverport"));
		jobEntryFTPPUT.setPassword(cell.getAttribute("password"));
		jobEntryFTPPUT.setServerName(cell.getAttribute("servername"));
		jobEntryFTPPUT.setProxyHost(cell.getAttribute("proxyserver"));
		jobEntryFTPPUT.setProxyPort(cell.getAttribute("proxyserverport"));
		jobEntryFTPPUT.setProxyUsername(cell.getAttribute("proxyserverusername"));
		jobEntryFTPPUT.setProxyPassword(cell.getAttribute("proxyserverpwd"));
		
		//一般---高级设置
		jobEntryFTPPUT.setBinaryMode("Y".equals(cell.getAttribute("binarymode") ));
		String timeoutString = cell.getAttribute("timeout");
		if( !StringUtils.isEmpty(timeoutString) ){
			boolean isDigital=false;
			try{ Integer.parseInt( timeoutString ) ;isDigital=true;}catch(Exception e){}
			 if( isDigital )
				 jobEntryFTPPUT.setTimeout( Integer.parseInt( timeoutString ) );
		}
		jobEntryFTPPUT.setActiveConnection("Y".equals(cell.getAttribute("useraliveftpconnection")));
		jobEntryFTPPUT.setControlEncoding(cell.getAttribute("controlencode"));
		
		//文件---源（本地）文件
		jobEntryFTPPUT.setLocalDirectory(cell.getAttribute("localdir"));
		jobEntryFTPPUT.setWildcard(cell.getAttribute("tongpeifu"));
		jobEntryFTPPUT.setRemove("Y".equals(cell.getAttribute("dellocalfileafterupload")));
		jobEntryFTPPUT.setOnlyPuttingNewFiles("Y".equals(cell.getAttribute("notcoverremotefiles")));
		
		//文件---目标（远程）文件
		jobEntryFTPPUT.setRemoteDirectory(cell.getAttribute("remotedir"));
		
		//Sockets代理 ---代理
		jobEntryFTPPUT.setProxyHost(cell.getAttribute("wProxy2server"));
		jobEntryFTPPUT.setProxyPort(cell.getAttribute("wProxy2serverport"));
		jobEntryFTPPUT.setProxyUsername(cell.getAttribute("proxy2serverusername"));
		jobEntryFTPPUT.setProxyPassword(cell.getAttribute("proxy2serverpwd"));

	}

	@Override
	public Element encode(JobEntryInterface jobEntry) throws Exception {
		JobEntryFTPPUT jobEntryFTPPUT = (JobEntryFTPPUT) jobEntry;

		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.JOB_JOBENTRY_NAME);
		//一般
		e.setAttribute("timeout", jobEntryFTPPUT.getTimeout()+"");
		e.setAttribute("serverport", jobEntryFTPPUT.getServerPort());
		//控制编码的处理
		e.setAttribute("contrlEncode",jobEntryFTPPUT.getControlEncoding() );
		System.out.println("控制编码my"+jobEntryFTPPUT.getControlEncoding());
		
		//文件
		//Sockets处理
		e.setAttribute("proxy2serverport", jobEntryFTPPUT.getProxyPort());
		
		return e;
	}

}
