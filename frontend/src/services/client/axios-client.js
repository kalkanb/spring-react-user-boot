import getClient from "./client";

class AxiosClient {
  constructor(options = null) {
    this.client = getClient(options);
  }

  async get(url, config = {}) {
    try {
      const response = await this.client.get(url, config);
      return response;
    } catch (error) {
      return Promise.reject(error);
    }
  }

  async delete(url, config = {}) {
    try {
      const response = await this.client.delete(url, config);
      return response;
    } catch (error) {
      return Promise.reject(error);
    }
  }

  async head(url, config = {}) {
    try {
      const response = await this.client.head(url, config);
      return response;
    } catch (error) {
      return Promise.reject(error);
    }
  }

  options(url, config = {}) {
    return this.client
      .options(url, config)
      .then((response) => response)
      .catch((error) => Promise.reject(error));
  }

  async post(url, data = {}, config = {}) {
    try {
      const response = await this.client.post(url, data, config);
      return response;
    } catch (error) {
      console.log(error);
      return Promise.reject(error);
    }
  }

  async put(url, data = {}, config = {}) {
    try {
      const response = await this.client.put(url, data, config);
      return response;
    } catch (error) {
      return Promise.reject(error);
    }
  }

  async patch(url, data = {}, config = {}) {
    try {
      const response = await this.client.patch(url, data, config);
      return response;
    } catch (error) {
      return Promise.reject(error);
    }
  }
}

const projectClient = new AxiosClient({
  baseURL: "http://localhost:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

export default projectClient;
