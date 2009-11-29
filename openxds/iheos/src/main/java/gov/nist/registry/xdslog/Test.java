package gov.nist.registry.xdslog;

import gov.nist.registry.common2.logging.LoggerException;


/**
 * Simple class to play with the logger.
 * @author jbmeyer
 *
 */
public class Test {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Log log = null ;
	  try 
	  {
	      for ( int i = 0 ; i < 150000 ; i++ )
	      {
		log = new Log("/workspace/xdsLogV2/gov/nist/registry/xdslog/ParamFile.txt"  ) ;
	   
		int finalIP = i%253 + 1 ;
		Message m = log.createMessage( "129.6.58."+finalIP ) ;
		
		m.setIP( "129.6.58." + finalIP ) ;
		m.setSecure((i%2==0)) ; 
		m.setCompany("PCJBM"+finalIP) ;
		m.setTestMessage("Test" + finalIP % 25 ) ;
		m.setPass((i%2==1)) ;
		try
		{
		 m.addParam( "http", "Test"  , "Test add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add values"  ) ;
		m.addParam( "http", "Test2"  , "Test add values"  ) ;
		m.addParam( "http", "Test3"  , "Test add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add valuesTest add values"  ) ;
		m.addOtherParam("What is Lorem Ipsum", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." ) ;
		m.addOtherParam("Why do we use it", "t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)." ) ;
		m.addOtherParam("Where does it come frome", "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32."+
"The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham." ) ;
		
		m.addOtherParam("Example", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean sollicitudin dapibus odio. Vestibulum hendrerit ultrices metus. Morbi varius, tellus vel tincidunt posuere, turpis enim interdum quam, nec suscipit lacus massa id mauris. Phasellus ac dui. Pellentesque sodales tempor dolor. Duis ipsum. Morbi sapien risus, imperdiet vitae, interdum non, hendrerit eget, lectus. Nam nec tellus. Nunc fringilla tristique sem. In hac habitasse platea dictumst. Nullam vehicula. Nunc elit. Proin neque turpis, aliquam id, aliquet et, auctor id, dui. Vestibulum ac turpis vel lacus pellentesque rhoncus. In hac habitasse platea dictumst. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."+
"Pellentesque mollis. Praesent non elit et erat porttitor volutpat. Nam eu tortor. Donec lobortis sagittis arcu. Quisque elementum. Fusce tellus. Proin aliquam ligula id dolor. Etiam euismod auctor diam. Praesent commodo volutpat massa. Morbi nec lorem eu erat interdum scelerisque. Morbi id mauris. Suspendisse potenti. Aenean eu lacus. Proin ultrices congue enim. Integer nec orci. ") ;	
		}
		catch ( LoggerException ex)
		{
			m.addErrorParam("LoggerException error", ex.getMessage() ) ;
			m.setPass(false) ;				
		}
		log.writeMessage(m) ;


		
		log.close() ;
	      }
	
	} catch (LoggerException logEx)
	{
		System.out.println(logEx.getMessage() ) ;
	}
	
	  catch (Exception e) {
		System.out.println(e.getMessage() ) ;
		if ( log!=null )
			try {
				log.close() ;
			} catch (LoggerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		 
	}
	  
	}

}
