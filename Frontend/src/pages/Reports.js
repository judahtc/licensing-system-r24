import React, { useState } from 'react';
import { Button, Row, Col, Card, Alert, Form } from 'react-bootstrap';
import { licenseService } from '../services/api';

function Reports() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [compareResult, setCompareResult] = useState(null);
  const [license1Id, setLicense1Id] = useState('');
  const [license2Id, setLicense2Id] = useState('');

  const downloadFile = (blob, filename) => {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  };

  const generatePdfReport = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await licenseService.generatePdfReport();
      downloadFile(response.data, 'licenses-report.pdf');
    } catch (err) {
      setError('Failed to generate PDF report');
    } finally {
      setLoading(false);
    }
  };

  const generateExcelReport = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await licenseService.generateExcelReport();
      downloadFile(response.data, 'licenses-report.xlsx');
    } catch (err) {
      setError('Failed to generate Excel report');
    } finally {
      setLoading(false);
    }
  };

  const generateCsvReport = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await licenseService.generateCsvReport();
      const blob = new Blob([response.data], { type: 'text/csv' });
      downloadFile(blob, 'licenses-report.csv');
    } catch (err) {
      setError('Failed to generate CSV report');
    } finally {
      setLoading(false);
    }
  };

  const compareLicenses = async () => {
    if (!license1Id || !license2Id) {
      setError('Please enter both license IDs');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const response = await licenseService.compareLicenses(license1Id, license2Id);
      setCompareResult(response.data.equal);
    } catch (err) {
      setError('Failed to compare licenses');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2>Reports & Tools</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col md={4}>
          <Card>
            <Card.Header>Generate PDF Report</Card.Header>
            <Card.Body>
              <p>Download a comprehensive PDF report of all licenses.</p>
              <Button 
                variant="primary" 
                onClick={generatePdfReport}
                disabled={loading}
              >
                {loading ? 'Generating...' : 'Download PDF'}
              </Button>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card>
            <Card.Header>Generate Excel Report</Card.Header>
            <Card.Body>
              <p>Download an Excel spreadsheet with license data.</p>
              <Button 
                variant="success" 
                onClick={generateExcelReport}
                disabled={loading}
              >
                {loading ? 'Generating...' : 'Download Excel'}
              </Button>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card>
            <Card.Header>Generate CSV Report</Card.Header>
            <Card.Body>
              <p>Download a CSV file for data analysis.</p>
              <Button 
                variant="info" 
                onClick={generateCsvReport}
                disabled={loading}
              >
                {loading ? 'Generating...' : 'Download CSV'}
              </Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col md={6}>
          <Card>
            <Card.Header>Compare Licenses</Card.Header>
            <Card.Body>
              <Form>
                <Row>
                  <Col>
                    <Form.Group className="mb-3">
                      <Form.Label>License 1 ID</Form.Label>
                      <Form.Control
                        type="number"
                        value={license1Id}
                        onChange={(e) => setLicense1Id(e.target.value)}
                        placeholder="Enter first license ID"
                      />
                    </Form.Group>
                  </Col>
                  <Col>
                    <Form.Group className="mb-3">
                      <Form.Label>License 2 ID</Form.Label>
                      <Form.Control
                        type="number"
                        value={license2Id}
                        onChange={(e) => setLicense2Id(e.target.value)}
                        placeholder="Enter second license ID"
                      />
                    </Form.Group>
                  </Col>
                </Row>
                <Button 
                  variant="warning" 
                  onClick={compareLicenses}
                  disabled={loading}
                >
                  {loading ? 'Comparing...' : 'Compare Licenses'}
                </Button>
              </Form>
              
              {compareResult !== null && (
                <Alert variant={compareResult ? 'success' : 'info'} className="mt-3">
                  <strong>Comparison Result:</strong> The licenses are {compareResult ? 'identical' : 'different'}.
                </Alert>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
}

export default Reports;