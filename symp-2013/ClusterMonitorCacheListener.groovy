import com.liferay.portal.kernel.cache.PortalCache
import com.liferay.portal.kernel.json.JSONFactoryUtil
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.cache.CacheListener;
import java.util.HashSet;
import java.util.Set;

public class ClusterMonitorCacheListener implements CacheListener {
	long _expectedPuts;
	Log _log;
	Set<String> keys;
	private boolean done=false;
	private Map<String, String> _result;

	public ClusterMonitorCacheListener(int clusterSize) {
		_log = LogFactoryUtil.getLog("ClusterMonitorCacheListener")
		keys = new HashSet<String>();
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
		keys.add(key.toString());
		_log.error("notifyEntryPut for key: " + key + ", value: " + value);
		if (keys.size() == _expectedPuts) {
			// process all data and create some aggregated data
			for (String k : keys) {
				_result.put(k, portalCache.get(k));
				_log.error("Recv data: " + k + " -> " + portalCache.get(k));
			}
			done=true;
		}
	}

	public JSONObject getResult() {
		JSONObject result = JSONFactoryUtil.createJSONObject();
		for (String k : keys) {
			result.put(k, JSONFactoryUtil.createJSONObject(_result.get(k)))
		}
		return result;
	}

	public String getResultAsString() {
	    return getResult().toString();
	}

	public void notifyEntryRemoved(
			PortalCache portalCache, Serializable key, Object value) {
		_log.error("notifyEntryRemoved for key: " + key + ", value: " + (Long)value);
		keys.remove(key.toString());
	}
	public void notifyEntryUpdated(
			PortalCache portalCache, Serializable key, Object value) {
		_log.error("notifyEntryUpdated for key: " + key + ", value: " + (Long)value);
	}
	public void notifyRemoveAll(PortalCache portalCache) {}
}
