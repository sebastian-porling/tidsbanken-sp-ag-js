
/**
 * Gives apporpiate url for webbserver for either prod or dev
 */
const API_URL = process.env.NODE_ENV === 'development' ? "http://localhost:3400/api/" : "/api/";

/**
 * Gives apporpiate url for sockets for either prod or dev
 */
const SOCKET_URL = process.env.NODE_ENV === 'development' ? "http://localhost:4121" : ":4121";

/**
 * Create authorization header
 * @param {String} token 
 */
const headers = (token) => {return {headers: {authorization: `Bearer ${token}`}}};

/**
 * Export all constants and functions
 */
export {
    API_URL,
    SOCKET_URL,
    headers
}