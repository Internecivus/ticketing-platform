import fetch from "isomorphic-unfetch";
import App from "next/app";
import Application from "./application";
import { Authentication } from "./authentication";

export class ApiError extends Error {
  constructor(text, response) {
    super(text);
    this.response = response;
  }
}

export const notifyIfError = async (promise) => {
  try {
    return await promise;
  } catch (e) {
    console.log(e);
  }
};

class API {
  async checkResponse(response) {
    if (response.ok) {
      if ([202, 204].includes(response.status)) {
        return response;
      }
      return response.json();
    }

    const error = new ApiError(response.statusText, response);
    return await notifyIfError(Promise.reject(error));
  }

  requestOptions() {
    const options = {
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    };

    if (Authentication.serverSideCookies) {
      options.headers.cookie = Authentication.serverSideCookies;
    }

    return options;
  }

  get(methodPath) {
    return fetch(methodPath, {
      method: "GET",
      ...this.requestOptions(),
    }).then(this.checkResponse);
  }

  remove(methodPath) {
    return fetch(methodPath, {
      method: "DELETE",
      ...this.requestOptions(),
    }).then(this.checkResponse);
  }

  post(methodPath, data = {}) {
    return fetch(methodPath, {
      method: "POST",
      ...this.requestOptions(),
      body: JSON.stringify(data),
    }).then(this.checkResponse);
  }

  put(methodPath, data = {}) {
    return fetch(methodPath, {
      method: "PUT",
      ...this.requestOptions(),
      body: JSON.stringify(data),
    }).then(this.checkResponse);
  }
}

class CrudAPI extends API {
  getOne(id) {
    return this.get(`${this.path}/${id}`);
  }

  getAll() {
    return this.get(this.path);
  }

  create(data) {
    return this.post(this.path, data);
  }

  delete(id) {
    return this.remove(`${this.path}/${id}`);
  }

  update(id, data) {
    return this.put(`${this.path}/${id}`, data);
  }
}

export const UserAPI = new (class extends CrudAPI {
  path = `${Application.serverDomain}/api/user`;

  getCurrentUser() {
    return this.getOne("current");
  }
})();

export const PasswordResetAPI = new (class extends API {
  path = `${Application.serverDomain}/api/passwordReset`;

  create(email) {
    return this.post(this.path, { email });
  }

  canBeUsed(token) {
    return this.post(`${this.path}/canBeUsed`, { token });
  }

  use({ tokenHash, newPassword }) {
    return this.post(`${this.path}/use`, { tokenHash, newPassword });
  }
})();

export const CategoriesAPI = new (class extends CrudAPI {
  path = `${Application.serverDomain}/api/category`;
})();

export const TicketsAPI = new (class extends CrudAPI {
  path = `${Application.serverDomain}/api/ticket`;

  getAllForCurrentUser() {
    return this.get(`${this.path}/currentUser`);
  }

  resolve(id) {
    return this.post(`${this.path}/${id}/resolve`);
  }
})();

export const GroupAPI = new (class extends CrudAPI {
  path = `${Application.serverDomain}/api/group`;
})();

export const AuthenticationAPI = new (class extends API {
  path = `${Application.serverDomain}/api/authentication`;

  login(usernameOrEmail, password) {
    return this.post(`${this.path}/login`, { usernameOrEmail, password });
  }

  logout() {
    return this.post(`${this.path}/logout`);
  }
})();
