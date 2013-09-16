/* Cluster monitor prepares a set of ClusterMonitor Commands and execute them in cluster */

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache
import com.liferay.portal.kernel.log.Log
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.CacheListenerScope;

public class ClusterMonitorCacheListener implements CacheListener {
	long _putsCount;
	long _expectedPuts;
	Log _log;
	List<Serializable> keys;

	public ClusterMonitorCacheListener(int clusterSize) {
		_putsCount=0
		_log = LogFactoryUtil.getLog("ClusterMonitorCacheListener")
		keys = new ArrayList<Serializable>();
		_log.error("Creating ClusterMonitorCacheListener, size: " + clusterSize)
		_expectedPuts = clusterSize;
	}

	public void notifyEntryEvicted(
			PortalCache portalCache, Serializable key, Object value) {
		_log.error("notifyEntryEvicted");
	}
	public void notifyEntryExpired(
			PortalCache portalCache, Serializable key, Object value) {
		_log.error("notifyEntryExpired");
	}

	public void notifyEntryPut(
			PortalCache portalCache, Serializable key, Object value) {
		_putsCount++;
		_log.error("np " + _putsCount);
		keys.add(key);
		if (_putsCount == _expectedPuts) {
			// process all data and create some aggregated data
			for (String k : keys) {
				_log.error("Recv data: " + k + " -> " + (Long)(portalCache.get(k)));
			}
			// output it
		}
	}
	public void notifyEntryRemoved(
			PortalCache portalCache, Serializable key, Object value) {
		_log.error("notifyEntryRemoved");
	}
	public void notifyEntryUpdated(
			PortalCache portalCache, Serializable key, Object value) {
		_log.error("notifyEntryUpdated");
	}
	public void notifyRemoveAll(PortalCache portalCache) {}
}

PortalCache pc = MultiVMPoolUtil.getCache("CLUSTER_MONITOR");
pc.removeAll();
String master = ClusterExecutorUtil.getLocalClusterNodeAddress().getRealAddress();
pc.registerCacheListener(new ClusterMonitorCacheListener(ClusterExecutorUtil.getClusterNodeAddresses().size()), CacheListenerScope.ALL);

sbCommand = new ScriptBuilder("https://raw.github.com/dsanz/scripts/cache/symp-2013/");
sbCommand.append("ScriptBuilder.groovy");
sbCommand.appendCode("master=\""+  master + "\"");
sbCommand.append("ClusterMonitorCommand.groovy");
sbCommand.runCluster();


