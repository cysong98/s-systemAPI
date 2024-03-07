import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_APP_API_URL;
const connectSSE = () => {
  let sse = new EventSource(`${API_BASE_URL}/sse/connect`);
  return sse;
};

const initializeSSE = () => {
  return axios.get(`${API_BASE_URL}/sse/initializeSSE`);
};

export { connectSSE, initializeSSE };
