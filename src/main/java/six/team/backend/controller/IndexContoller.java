package six.team.backend.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import six.team.backend.PageJsonGen;
import six.team.backend.model.Index;
import six.team.backend.store.IndexStore;
import six.team.backend.store.PageStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class IndexContoller {
	//this endpoint returns details for the homepage
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public
	@ResponseBody
	ResponseEntity<IndexStore> index() {
			IndexStore index = Index.getIndex();
			return new ResponseEntity<IndexStore>(index, HttpStatus.OK);
		}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	//this endpoint allows for the homepage to be editted
	public
	@ResponseBody ResponseEntity<String> editIndex(HttpServletRequest request,HttpServletResponse res) {
		IndexStore index = new IndexStore();
		String title = request.getHeader("title");
		String description = request.getHeader("description");
		int id = Integer.parseInt(request.getHeader("id"));
		index.setTitle(title);
		index.setDescription(description);
		index.setId(id);
		boolean result = Index.updateIndex(index);
		JSONObject object = new JSONObject();
		object.put("updated", result);
		return new ResponseEntity<String>(object.toString(), HttpStatus.OK);
	}


}
