package com.zds.treedemo;

import com.zds.treedemo.domain.TagCategoryDO;
import com.zds.treedemo.mapper.TagCategoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TreedemoApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
class TreedemoApplicationTests {

	@Autowired
	private TagCategoryMapper tagCategoryMapper;

	private static List<TagCategoryDO> treeList = new ArrayList<>();  //全局变量

	@Test
	void contextLoads() {

	}

	@Test
	public void testGetAll(){
		// 获取所有标签分类
		List<TagCategoryDO> tagCategoryDOS = tagCategoryMapper.selectAll();
		// 传入想要获取的标签分类的逐级子分类的parent_id
		Integer pid = 68;
		ArrayList<TagCategoryDO> childList = new ArrayList<>();

		// 递归的终止条件 找到一个分类他的id 没有任何一个分类是以他为parent_id的
//		getAllNewsClass(tagCategoryDOS,68,childList).stream().forEach(item->{
//			System.out.println("---level2"+item);
//			getAllNewsClass(tagCategoryDOS,item.getId(),childList).stream().forEach(item1->{
//				System.out.println("---level3"+item1);
//			});
//		});

		getChild(pid,tagCategoryDOS).stream().forEach(item->{
			System.out.println(item);
		});

//		orgRecursion(childList,tagCategoryDOS,68);


	}

	@Test
	public void testTree()throws Exception{


	}

	// 获取某一个节点的相邻子节点 但是没法往下递归
	public static List<TagCategoryDO> getAllNewsClass(List<TagCategoryDO> allList, int pid,List<TagCategoryDO> childList) {
		for (TagCategoryDO tagCategoryDO : allList) {
			//遍历出父id等于参数的id,add进子节点集合
			if (Integer.valueOf(tagCategoryDO.getParentId()) == pid) {
				childList.add(tagCategoryDO);
				//递归遍历下一级
				getAllNewsClass(allList, tagCategoryDO.getId(),childList);
			}
		}
		return childList;
	}

	private static void orgRecursion(List<TagCategoryDO> childOrg,List<TagCategoryDO> orgList, int pid) {
		for (TagCategoryDO org : orgList) {
			if (org.getParentId() != null) {
				//遍历出父id等于参数的id，add进子节点集合
				if (org.getParentId() == pid) {
					//递归遍历下一级
					orgRecursion(childOrg,orgList, org.getId());
					//末级机构才添加进去(依自己业务定义)
					if (org.getLevel() == 3) {
						childOrg.add(org);
					}
				}
			}
		}
	}



	public static List<TagCategoryDO> getTree(List<TagCategoryDO> menu,List<TagCategoryDO> parentList) {
		//先获取到所有数据
//        treeList=MenuMapper.getList();
		if(menu==null) return null;

		//获取到所有一级节点
//        List<Menu> parentList = this.MenuMapper.findParentList();
		List<TagCategoryDO> list = new ArrayList<>();
		if(parentList != null){
			for (int i = 0; i < parentList.size(); i++) {
				list.add(recursiveTree(parentList.get(i).getId()));
			}
		}
		return list;
	}

	/**
	 * 递归算法解析成树形结构
	 * @param cid
	 */
	public static TagCategoryDO recursiveTree(Integer cid) {
		TagCategoryDO node = getMenuById(cid);
		List<TagCategoryDO> childTreeNodes  = getChildTreeById(cid);
		for(TagCategoryDO child : childTreeNodes){
			TagCategoryDO n = recursiveTree(child.getId());
			List<TagCategoryDO> list = new ArrayList<TagCategoryDO>();
			list.add(n);
			System.out.println(node.getChildList());
			if (node.getChildList()==null) {
				List<TagCategoryDO> li = new ArrayList<TagCategoryDO>();
				node.setChildList(li);
			}
//            node.setChildren(list);
			node.getChildList().add(n);
		}
		return node;
	}

	/**
	 * 根据CID查询节点对象
	 */
	public static TagCategoryDO getMenuById(Integer cid){
		Map map =  getTreeMap();
		return (TagCategoryDO) map.get(cid);
	}

	/**
	 * 一次性取所有数据，为了减少对数据库查询操作
	 * @return
	 */
	public static Map getTreeMap(){
		Map map =  new HashMap<Integer, TagCategoryDO>();
		if(null != treeList){
			for(TagCategoryDO d : treeList){
				map.put(d.getId(), d);
			}
		}
		return map;
	}
	/**
	 * 根据父节点CID获取所有了节点
	 */
	public static List<TagCategoryDO> getChildTreeById(Integer cid){
		List<TagCategoryDO> list = new ArrayList<>();
		if(null != treeList){
			for (TagCategoryDO d : treeList) {
				if(null != cid){
					if (cid.equals(Integer.valueOf(d.getParentId()))) {
						list.add(d);
					}
				}
			}
		}
		return list;
	}


	/**
	 * 获取子节点
	 * @param id 父节点id
	 * @param allMenu 所有菜单列表
	 * @return 每个根节点下，所有子菜单列表
	 */
	public List<TagCategoryDO> getChild(Integer id,List<TagCategoryDO> allMenu){
		//子菜单
		List<TagCategoryDO> childList = new ArrayList<>();
		for (TagCategoryDO nav : allMenu) {
			// 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
			//相等说明：为该根节点的子节点。
			if(nav.getParentId().equals(id)){
				childList.add(nav);
			}
		}
		//递归
		for (TagCategoryDO nav : childList) {
			nav.setChildList(getChild(nav.getId(), allMenu));
		}
		//如果节点下没有子节点，返回一个空List（递归退出）
		if(childList.size() == 0){
			return new ArrayList<>();
		}
		return childList;
	}

	public Map<String,Object> findTree(){
		Map<String,Object> data = new HashMap<String,Object>();
		try {//查询所有菜单
			List<TagCategoryDO> allMenu = tagCategoryMapper.selectAll();
			//根节点
			List<TagCategoryDO> rootMenu = new ArrayList<>();
			for (TagCategoryDO nav : allMenu) {
				if(nav.getParentId().equals("0")){//父节点是0的，为根节点。
					rootMenu.add(nav);
				}
			}
			//为根菜单设置子菜单，getClild是递归调用的
			for (TagCategoryDO nav : rootMenu) {
				/* 获取根节点下的所有子节点 使用getChild方法*/
				List<TagCategoryDO> childList = getChild(nav.getId(), allMenu);
				nav.setChildList(childList);//给根节点设置子节点
			}
			/**
			 * 输出构建好的菜单数据。
			 *
			 */
			data.put("success", "true");
			data.put("list", rootMenu);
			return data;
		} catch (Exception e) {
			data.put("success", "false");
			data.put("list", new ArrayList());
			return data;
		}
	}

	@Test
	public void TestGetTree()throws Exception{

		Map<String, Object> tree = findTree();
		for (String s : tree.keySet()) {
			
		}
		
	}

}
