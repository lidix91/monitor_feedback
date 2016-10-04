package ch.uzh.ifi.feedback.orchestrator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.ServletBase;
import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;

/**
 * Servlet implementation class OrchestratorServlet
 */
@Singleton
public class OrchestratorServlet extends ServletBase {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
	@Inject
    public OrchestratorServlet(IRestManager restManager, DatabaseConfiguration config) {
        super(restManager, config);
    }
    
    @Override
    protected void InitController()
    {
        try{
        	_restManager.Init("ch.uzh.ifi.feedback.orchestrator");
        }
        catch(Exception ex){
        	System.out.println(ex.getMessage());
        	ex.printStackTrace();
        	super.destroy();
        }
    }
}
