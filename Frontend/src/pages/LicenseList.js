import React, { useState, useEffect } from 'react';
import { Table, Button, Form, Row, Col, Alert, Modal } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { licenseService } from '../services/api';

function LicenseList() {
  const [licenses, setLicenses] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showAdjustModal, setShowAdjustModal] = useState(false);
  const [selectedLicense, setSelectedLicense] = useState(null);
  const [adjustmentPercentage, setAdjustmentPercentage] = useState('');

  useEffect(() => {
    fetchLicenses();
  }, []);

  const fetchLicenses = async () => {
    try {
      const response = await licenseService.getAllLicenses();
      setLicenses(response.data);
      setLoading(false);
    } catch (err) {
      setError('Failed to fetch licenses');
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (searchTerm.trim()) {
      try {
        const response = await licenseService.searchLicenses(searchTerm);
        setLicenses(response.data);
      } catch (err) {
        setError('Search failed');
      }
    } else {
      fetchLicenses();
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this license?')) {
      try {
        await licenseService.deleteLicense(id);
        fetchLicenses();
      } catch (err) {
        setError('Failed to delete license');
      }
    }
  };

  const handleAdjustFee = async () => {
    try {
      await licenseService.adjustApplicationFee(selectedLicense.id, parseFloat(adjustmentPercentage));
      setShowAdjustModal(false);
      setAdjustmentPercentage('');
      fetchLicenses();
    } catch (err) {
      setError('Failed to adjust fee');
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <Row className="mb-3">
        <Col md={8}>
          <h2>License Management</h2>
        </Col>
        <Col md={4} className="text-end">
          <Link to="/licenses/new" className="btn btn-primary">
            Add New License
          </Link>
        </Col>
      </Row>

      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-3">
        <Col md={8}>
          <Form.Control
            type="text"
            placeholder="Search by company name..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </Col>
        <Col md={4}>
          <Button onClick={handleSearch} variant="outline-primary">
            Search
          </Button>
          <Button onClick={fetchLicenses} variant="outline-secondary" className="ms-2">
            Clear
          </Button>
        </Col>
      </Row>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Company Name</th>
            <th>License Type</th>
            <th>Issue Date</th>
            <th>Email</th>
            <th>Application Fee</th>
            <th>Years Before Expiry</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {licenses.map((license) => (
            <tr key={license.id}>
              <td>{license.companyName}</td>
              <td>{license.licenseType}</td>
              <td>{license.issueDate}</td>
              <td>{license.email}</td>
              <td>${license.applicationFee}</td>
              <td>{license.yearsBeforeExpiry}</td>
              <td>
                <Link to={`/licenses/edit/${license.id}`} className="btn btn-sm btn-outline-primary me-2">
                  Edit
                </Link>
                <Button
                  size="sm"
                  variant="outline-warning"
                  className="me-2"
                  onClick={() => {
                    setSelectedLicense(license);
                    setShowAdjustModal(true);
                  }}
                >
                  Adjust Fee
                </Button>
                <Button
                  size="sm"
                  variant="outline-danger"
                  onClick={() => handleDelete(license.id)}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showAdjustModal} onHide={() => setShowAdjustModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Adjust Application Fee</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>Adjustment Percentage</Form.Label>
              <Form.Control
                type="number"
                step="0.01"
                value={adjustmentPercentage}
                onChange={(e) => setAdjustmentPercentage(e.target.value)}
                placeholder="Enter percentage (e.g., 10 for 10% increase)"
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowAdjustModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleAdjustFee}>
            Adjust Fee
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default LicenseList;