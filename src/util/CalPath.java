package util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CalPath {
	
	public List<List<String>> list = new ArrayList<>();
	public List<String> maxlist;
	
	public List<List<String>> searchPath(List<String> s){
		Map<String, Set<String>> map = new HashMap<>();
		for (int i = 0; i < s.size(); i++) {
			String[] ss = s.get(i).split(",");
			if (!map.containsKey(ss[0]+ss[2])) {
				Set<String> set = new HashSet<>();
				set.add(ss[3]+ss[5]);
				map.put(ss[0]+ss[2], set);
			} else {
				map.get(ss[0]+ss[2]).add(ss[3]+ss[5]);
			}
		}
		Set<String> keyset = map.keySet();
		List<String> marked = new ArrayList<String>();
		for (String key : keyset) {
			int[] len={0};
			if(marked.contains(key)) continue;
			list.add(dfs(key, map, marked,new ArrayList<String>(),len));
		}
//		for (List<String> sl : list) {
//			for (String str : sl) {
//				System.out.print(str+" ");
//			}
//			System.out.println();
//		}
		//System.out.println(list.size());
		return list;
	}

	public List<String> dfs(String key, Map<String, Set<String>> map, List<String> marked,List<String> slist,int[] len) {
		if (marked.contains(key)) {
			if(slist.size()>len[0]){
				maxlist=new ArrayList<>(slist);
				len[0]=slist.size();
			}
		}else {
			marked.add(key);
			slist.add(key);
			Set<String> values = map.get(key);
			if (values != null) {
				for (String value : values) {
					dfs(value, map, marked,slist,len);
				}
			}else{
				if(slist.size()>len[0]){
					maxlist=new ArrayList<>(slist);
					len[0]=slist.size();
				}
			}
			slist.remove(key);
		}
		return maxlist;
	}
}
