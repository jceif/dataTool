package service.table;

import org.springframework.stereotype.Service;


public interface GenDubboXMLService {
	   String geneCustomerXML(String schemaName, String target) throws Exception;

	   String geneProducerXML(String schemaName, String target) throws Exception;



}
