/**
 * 
 */
package srkarra.cmpe283.p1.config;

/**
 * @author Asim Mughni
 *
 */
public class RestApi {

    public static class HTTPPostResponse {

        private int responseCode;

        private String responseData;

        public HTTPPostResponse(int i) {
            this.responseCode = i;
        }
 
        public HTTPPostResponse(int responseCode, String responseData) {
            this.responseCode = responseCode;
            this.responseData = responseData;
        }
 
        public int getResponseCode() {
            return responseCode;
        }
 
        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }
 
        public String getResponseData() {
            return responseData;
        }
        
        
 
        /* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "HTTPPostResponse [responseCode=" + responseCode
					+ ", responseData=" + responseData + "]";
		}

		public void setResponseMsg(String responseData) {
            this.responseData = responseData;
        }
    }
}
