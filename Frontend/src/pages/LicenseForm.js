import React, { useState, useEffect } from 'react';
import { Form, Button, Row, Col, Alert } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import { licenseService } from '../services/api';

function LicenseForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEdit = Boolean(id);

  const [formData, setFormData] = useState({
    companyName: '',
    issueDate: '',
    latitude: '',
    longitude: '',
    email: '',
    licenseType: 'CTL',
    validityPeriod: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEdit) {
      fetchLicense();
    }
  }, [id, isEdit]);

  const fetchLicense = async () => {
    try {
      const response = await licenseService.getLicenseById(id);
      setFormData(response.data);
    } catch (err) {
      setError('Failed to fetch license details');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (isEdit) {
        await licenseService.updateLicense(id, formData);
      } else {
        await licenseService.createLicense(formData);
      }
      navigate('/licenses');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save license');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2>{isEdit ? 'Edit License' : 'Add New License'}</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Row>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Company Name *</Form.Label>
              <Form.Control
                type="text"
                name="companyName"
                value={formData.companyName}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>License Type *</Form.Label>
              <Form.Select
                name="licenseType"
                value={formData.licenseType}
                onChange={handleChange}
                required
              >
                <option value="CTL">Cellular Telecommunication License (CTL)</option>
                <option value="PRSL">Public Radio Station License (PRSL)</option>
              </Form.Select>
            </Form.Group>
          </Col>
        </Row>

        <Row>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Issue Date *</Form.Label>
              <Form.Control
                type="date"
                name="issueDate"
                value={formData.issueDate}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Email *</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
        </Row>

        <Row>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Latitude *</Form.Label>
              <Form.Control
                type="number"
                step="any"
                name="latitude"
                value={formData.latitude}
                onChange={handleChange}
                placeholder="e.g., -17.8292"
                required
              />
            </Form.Group>
          </Col>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Longitude *</Form.Label>
              <Form.Control
                type="number"
                step="any"
                name="longitude"
                value={formData.longitude}
                onChange={handleChange}
                placeholder="e.g., 31.0522"
                required
              />
            </Form.Group>
          </Col>
        </Row>

        {formData.licenseType === 'PRSL' && (
          <Row>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label>Validity Period (Years) *</Form.Label>
                <Form.Control
                  type="number"
                  name="validityPeriod"
                  value={formData.validityPeriod}
                  onChange={handleChange}
                  required={formData.licenseType === 'PRSL'}
                />
              </Form.Group>
            </Col>
          </Row>
        )}

        <div className="d-flex gap-2">
          <Button type="submit" variant="primary" disabled={loading}>
            {loading ? 'Saving...' : (isEdit ? 'Update License' : 'Create License')}
          </Button>
          <Button type="button" variant="secondary" onClick={() => navigate('/licenses')}>
            Cancel
          </Button>
        </div>
      </Form>
    </div>
  );
}

export default LicenseForm;