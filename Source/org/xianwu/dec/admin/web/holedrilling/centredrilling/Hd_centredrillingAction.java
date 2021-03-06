package org.xianwu.dec.admin.web.holedrilling.centredrilling;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xianwu.core.json.JsonHelper;
import org.xianwu.core.metatype.Dto;
import org.xianwu.core.metatype.impl.BaseDto;
import org.xianwu.core.mvc.xstruts.action.ActionForm;
import org.xianwu.core.mvc.xstruts.action.ActionForward;
import org.xianwu.core.mvc.xstruts.action.ActionMapping;
import org.xianwu.core.mvc.xstruts.upload.FormFile;
import org.xianwu.core.util.Utils;
import org.xianwu.core.web.BizAction;
import org.xianwu.core.web.CommonActionForm;
import org.xianwu.core.web.image.DeleteFile;
import org.xianwu.core.web.image.UploadImage;
import org.xianwu.core.web.report.excel.ExcelImport;
import org.xianwu.core.web.util.WebUtils;
import org.xianwu.dec.admin.service.Hd_centredrillingService;

/**
 * 中心钻
 * 
 * @author XianwuFu
 * @since 2013-01-01
 * @see BizAction
 */
public class Hd_centredrillingAction extends BizAction {

	private Hd_centredrillingService hd_centredrillingService = (Hd_centredrillingService) super.getService("hd_centredrillingService");
	private String filepath = "\\dec\\image\\hd_centredrilling\\";

	/**
	 * 中心钻
	 * 
	 * @param
	 * @return
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.removeSessionAttribute(request, "PAGE_PARAMETER_DTO_HD_CENTREDRILLING_HD_CENTREDRILLING"); // 修正分页传参
		// -
		// 1
		// /
		// 5
		return mapping.findForward("hd_centredrillingView");
	}

	/**
	 * 中心钻
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public ActionForward queryHd_centredrilling4Manage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);

		// 修正分页传参 - 2 / 5
		Dto dto_page = (BaseDto) super.getSessionAttribute(request, "PAGE_PARAMETER_DTO_HD_CENTREDRILLING_HD_CENTREDRILLING");
		if (Utils.isNotEmpty(dto_page) && Utils.isNotEmpty(dto)) {
			dto = WebUtils.getDtoDataFromFirst2Second(dto, dto_page);
		}

		// 修正分页传参 - 3 / 5
		super.removeSessionAttribute(request, "PAGE_PARAMETER_DTO_HD_CENTREDRILLING_HD_CENTREDRILLING");
		super.setSessionAttribute(request, "PAGE_PARAMETER_DTO_HD_CENTREDRILLING_HD_CENTREDRILLING", dto);
		List Hd_centredrillingList = g4Reader.queryForPage("Hd_centredrilling.queryHd_centredrilling4Manage", dto);
		Integer totalInteger = (Integer) g4Reader.queryForObject("Hd_centredrilling.queryHd_centredrilling4Manage4PageCount", dto);
		String jsonString = JsonHelper.encodeList2PageJson(Hd_centredrillingList, totalInteger, null);
		write(jsonString, response);
		
		return mapping.findForward(null);
	}

	/**
	 * 保存中心钻
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward saveHd_centredrilling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;

		Dto inDto = aForm.getParamAsDto(request);
		inDto.put("figure", UploadImage.upload(request, aForm.getFigure(), filepath, inDto.getAsInteger("compressflag")));
		inDto.put("figure2", UploadImage.upload(request, aForm.getFigure2(), filepath, inDto.getAsInteger("compressflag2")));

		Dto outDto = hd_centredrillingService.saveHd_centredrilling(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}

	/**
	 * 删除中心钻
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward deleteHd_centredrilling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);

		String msg = DeleteFile.delete(request, strChecked, "toolholderid", "Hd_centredrilling.queryFigure4Delete4Hd_centredrilling");

		hd_centredrillingService.deleteHd_centredrilling(inDto);

		setOkTipMsg("中心钻删除成功！" + msg, response);
		return mapping.findForward(null);
	}

	/**
	 * 修改中心钻
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward updateHd_centredrilling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;

		Dto inDto = aForm.getParamAsDto(request);
		String fig = UploadImage.upload(request, aForm.getFigure(), filepath, inDto.getAsInteger("compressflag"));
		if (Utils.isEmpty(fig)) {
			inDto.remove("figure");
		} else {
			inDto.put("figure", fig);
		}
		String fig2 = UploadImage.upload(request, aForm.getFigure2(), filepath, inDto.getAsInteger("compressflag2"));
		if (Utils.isEmpty(fig)) {
			inDto.remove("figure2");
		} else {
			inDto.put("figure2", fig2);
		}
		hd_centredrillingService.updateHd_centredrilling(inDto);
		setOkTipMsg("中心钻修改成功！", response);
		return mapping.findForward(null);
	}

	/**
	 * 查询品牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward queryBrand4Hd_centredrilling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);
		List list = g4Reader.queryForPage("Hd_centredrilling.queryBrand4Hd_centredrilling", dto);
		String jsonString = JsonHelper.encodeList2PageJson(list, list.size(), null);
		super.write(jsonString, response);
		return mapping.findForward(null);
	}

	/**
	 * 查询切削液
	 * 
	 * @param
	 * @return
	 */
	
	/* * @SuppressWarnings({ "rawtypes" }) public ActionForward
	 * queryCategory4Milling_blade_clamp(ActionMapping mapping, ActionForm form,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { CommonActionForm aForm = (CommonActionForm) form; Dto dto =
	 * aForm.getParamAsDto(request);
	 * 
	 * List Milling_blade_clampList =
	 * g4Reader.queryForPage("Milling_blade_clamp.queryCategory4Milling_blade_clamp"
	 * , dto); String jsonString =
	 * JsonHelper.encodeList2PageJson(Milling_blade_clampList,
	 * Milling_blade_clampList.size(), null); write(jsonString, response);
	 * return mapping.findForward(null); }*/
	/**
	 * 导入中心钻数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward importHd_centredrillingFromExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommonActionForm actionForm = (CommonActionForm) form;
		FormFile theFile = actionForm.getTheFile();
		Dto dto = (BaseDto) actionForm.getParamAsDto(request);

		String fielddata = dto.getAsString("nameandid");
		ExcelImport ei = new ExcelImport();
		
		Dto dtos = new BaseDto();
		
		if (fielddata.equalsIgnoreCase("initializeImportExcelData")) {
			Dto snDto = ei.initializeExcel(theFile);
			
			write(snDto.toJson(), response);
		} else {
			Dto nullFlag = new BaseDto();
			nullFlag.put("mtmodel", true);// 表示assetscode字段不能为空，默认所有都可为空；
			//nullFlag.put("assetscode", true);
			Dto codeFlag = new BaseDto();
			//codeFlag.put("remark", true); // 表示字段sex在code表中，在Excel中的数据要按code表中已有的值，
								     // 比如在Excel中，性别只能取[男, 女, 未知]三种，而该三种值对应code表中性别字段的描述信息；
			String importdata = dto.getAsString("importdata");
			int importStart = 2;
			int importTo = Integer.parseInt(importdata);

			/*String[] importdata = dto.getAsString("importdata").split(",");
			
			int importStart = Integer.parseInt(importdata[0]);
			int importTo = Integer.parseInt(importdata[1]);*/
			String sheetName = dto.getAsString("sheetname");
			
			// 使用说明：
			// fielddata    - (required) 页面传回的grid的表头信息，比如，姓名,性别-dec-name,sex；
			// theFile      - (required) 上传的要导入数据的Excel文件；
			// nullFlag     - (optional) 字段是否不为空，是为了检测Excel中数据的正确性；
			// codeFlag     - (optional) 哪些字段是在code表中，如果在code表中，请手动指明；
			// sheetName    - (optional) Excel中要导入数据的sheet名称或者索引，从1开始，如果未指明，则导入第一个；
			// importStart  - (optional) Excel中开始导入数据的行数，如果未指定，则从第二行开始导入，会认为第一行为表头；
			// importTo     - (optional) Excel中导入数据到第几行，如果未指定或指定为-1，则导入所有行；
			Dto resultDto = ei.importExcel(request, fielddata, theFile, nullFlag, codeFlag, sheetName, importStart, importTo);

			List<Dto> dataList = resultDto.getAsList("data");

			// Excel导入方法介绍，第一种：使用For循环导入
			// if (resultDto.getAsBoolean("issuccess")) {
			// 	for (int jj = 0; jj < dataList.size(); jj++) {
			// 		Dto dto3 = (Dto) dataList.get(jj);

			// 		// 数据导入
			// 		machinetoolService.saveMachinetool(dto3);
			// 	}

			// 	msg = "选择的要导入的数据信息导入成功!";
			// 	dtos.put("success", true);
			// } else {
			// 	dtos.put("failure", false);
			// 	msg = resultDto.getAsString("msg");
			// }
			// 
			// dtos.put("msg", msg);
			

			// 第二种方法，批量导入，推荐使用
			dtos = hd_centredrillingService.batchSaveHd_centredrilling(dataList);
			
			write(dtos.toJson(), response);
		}
		return mapping.findForward(null);
	}
}
