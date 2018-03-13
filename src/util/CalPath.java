package util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CalPath {
	
	public List<List<String>> list = new ArrayList<>();//存储以每个节点为起点的最长路径
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
		for (String key : keyset) {//以每个节点为起始节点进行遍历
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
/*key代表图中的节点
 *map被遍历的图
 *slist用于储存深度遍历得到路径结果
 *marked 标记节点是否已经被遍历过
 *len 记录最长路径的长度
*/ 
	public List<String> dfs(String key, Map<String, Set<String>> map, List<String> marked,List<String> slist,int[] len) {
		if (marked.contains(key)) {//当key节点已经被遍历过，该路径结束
			if(slist.size()>len[0]){//当该路径长度大于现存路径的长度，进行存储
				maxlist=new ArrayList<>(slist);
				len[0]=slist.size();
			}
		}else {
			marked.add(key);//进行标记节点
			slist.add(key);//将节点加入路径中
			Set<String> values = map.get(key);//获取该节点的邻居节点
			if (values != null) {
				for (String value : values) {//对邻居节点进行遍历
					dfs(value, map, marked,slist,len);
				}
			}else{//当节点没有邻居节点，该路径遍历结束。并进行和上述相同的路径长度判断
				if(slist.size()>len[0]){
					maxlist=new ArrayList<>(slist);
					len[0]=slist.size();
				}
			}
			slist.remove(key);//节点邻居遍历结束后，从路径中删除该节点
		}
		return maxlist;
	}
}
