package org.xianwu.dec.admin.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.xianwu.core.metatype.Dto;
import org.xianwu.core.metatype.impl.BaseDto;
import org.xianwu.core.model.service.impl.BizServiceImpl;
import org.xianwu.core.orm.xibatis.sqlmap.client.SqlMapExecutor;
import org.xianwu.core.orm.xibatis.support.SqlMapClientCallback;
import org.xianwu.dec.admin.service.CuttoolholderService;
import org.xianwu.system.common.util.idgenerator.MaxId;

/**
 * 切断切槽刀体
 * 
 * @author XianwuFu
 * @since 2013-01-01
 */
public class CuttoolholderServiceImpl extends BizServiceImpl implements CuttoolholderService {

	/**
	 * 保存刀体
	 * 
	 * @param pDto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Dto saveCuttoolholder(Dto pDto) {
		Dto outDto = new BaseDto();
		pDto.put("toolholderid", MaxId.getCuttoolholderID());
		g4Dao.insert("Cuttoolholder.saveCuttoolholder", pDto);
		outDto.put("msg", "刀体新增成功！");
		outDto.put("success", new Boolean(true));
		return outDto;
	}
	/**
	 * 批量保存刀体
	 *
	 * @param pDto
	 * @return
	 */
	public Dto batchSaveCuttoolholder(final List<Dto> dataList) {
		final Dto outDto = new BaseDto();
		g4Dao.getSqlMapClientTpl().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (int i = 0; i < dataList.size(); i++) {
					Dto dto3 = (Dto) dataList.get(i);
					dto3.put("toolholderid", MaxId.getCuttoolholderID());
					//dto3.put("mainshaftpower", null);
					executor.insert("Cuttoolholder.saveCuttoolholder", dto3);
				}
				executor.executeBatch();
				
				outDto.put("msg", "批量导入刀体成功！");
				outDto.put("success", true);
				return null;
			}
		});
		return outDto;
	}
	
	/**
	 * 保存刀体
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto insertCuttoolholder(Dto pDto) {
		return saveCuttoolholder(pDto);
	}
	
	/**
	 * 删除刀体
	 * 
	 * @param pDto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Dto deleteCuttoolholder(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("toolholderid", arrChecked[i]);
			//g4Dao.update("Cuttoolholder.updateMachinecuttoolholder4Cuttoolholder", dto);
			//g4Dao.update("Cuttoolholder.updateBladecuttoolholder4Cuttoolholder", dto);
			g4Dao.delete("Cuttoolholder.deleteCuttoolholder", dto);
		}
		return null;
	}

	/**
	 * 修改刀体
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto updateCuttoolholder(Dto pDto) {
		
		g4Dao.update("Cuttoolholder.updateCuttoolholder", pDto);
		return null;
	}
	
	public Dto saveMainTable(Dto dto){
		g4Dao.update("Cuttoolholder.saveMainTable", dto);
		return null;
	}

	
	public Dto saveSubTable(Dto dto){
		g4Dao.update("Cuttoolholder.saveSubTable", dto);
		return null;
	}
}
