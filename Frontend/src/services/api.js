import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const licenseService = {
  getAllLicenses: () => api.get('/licenses'),
  getLicenseById: (id) => api.get(`/licenses/${id}`),
  createLicense: (license) => api.post('/licenses', license),
  updateLicense: (id, license) => api.put(`/licenses/${id}`, license),
  deleteLicense: (id) => api.delete(`/licenses/${id}`),
  searchLicenses: (companyName) => api.get(`/licenses/search?companyName=${companyName}`),
  adjustApplicationFee: (id, percentage) => api.put(`/licenses/${id}/adjust-fee`, { percentage }),
  compareLicenses: (id1, id2) => api.get(`/licenses/compare/${id1}/${id2}`),
  getLicensesExpiring: (days = 30) => api.get(`/licenses/expiring?days=${days}`),
  generatePdfReport: () => api.get('/licenses/reports/pdf', { responseType: 'blob' }),
  generateExcelReport: () => api.get('/licenses/reports/excel', { responseType: 'blob' }),
  generateCsvReport: () => api.get('/licenses/reports/csv'),
};

export default api;