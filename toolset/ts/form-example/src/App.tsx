import React, { useState } from 'react'
import { FormInput } from './components/FormInput'
import { InputType } from './components/InputType'
import { ReportName } from './ReportName.tsx'

interface ReportNew {
  name: ReportName
  checkbox: boolean
  radio: string
  dropdown: string
  date: string
}

const AppNew: React.FC = () => {
  const [isFormOpen, setIsFormOpen] = useState(false)
  const allReports = Object.values(ReportName)

  // Initialize form state dynamically based on ReportNames
  const [form, setForm] = useState<ReportNew[]>(
    allReports.map((name) => ({
      name,
      checkbox: false,
      radio: '',
      dropdown: '',
      date: '',
    }))
  )

  const [activeAccordions, setActiveAccordions] = useState<number[]>([])

  const handleFormToggle = () => {
    setIsFormOpen(!isFormOpen)
  }

  // Toggle accordion visibility and clear state when collapsing
  const handleAccordionToggle = (index: number) => {
    setActiveAccordions((prevActiveAccordions) => {
      if (prevActiveAccordions.includes(index)) {
        setForm((prevForm) =>
          prevForm.map((report, idx) =>
            idx === index
              ? {...report, checkbox: false, radio: '', dropdown: '', date: ''}
              : report
          )
        )
        return prevActiveAccordions.filter((i) => i !== index)
      } else {
        return [...prevActiveAccordions, index]
      }
    })
  }

  const handleInputChange = (index: number, field: keyof ReportNew, value: unknown) => {
    setForm((prevForm) =>
      prevForm.map((report, idx) =>
        idx === index ? {...report, [field]: value} : report
      )
    )
  }

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    console.log('Form Data:', form)
  }

  return (
    <div>
      <button type="button" onClick={handleFormToggle}>
        {isFormOpen ? 'Close Form' : 'Open Form'}
      </button>

      {isFormOpen && (
        <form onSubmit={handleSubmit}>
          {form.map((report, index) => (
            <div key={index}>
              <button type="button" onClick={() => handleAccordionToggle(index)}>
                {activeAccordions.includes(index)
                  ? `Collapse ${report.name}`
                  : `Expand ${report.name}`}
              </button>
              {activeAccordions.includes(index) && (
                <div>
                  <h3>{report.name}</h3>
                  <FormInput
                    type={InputType.CHECKBOX}
                    label={`${report.name} Checkbox`}
                    checked={report.checkbox}
                    onChange={(checked) =>
                      handleInputChange(index, 'checkbox', checked)
                    }
                  />
                  <FormInput
                    type={InputType.RADIO}
                    name={`radio${report.name}`}
                    options={['Option 1', 'Option 2']}
                    selectedOption={report.radio}
                    onChange={(value) =>
                      handleInputChange(index, 'radio', value)
                    }
                  />
                  <FormInput
                    type={InputType.DROPDOWN}
                    label={`${report.name} Dropdown`}
                    options={['Option 1', 'Option 2']}
                    selectedOption={report.dropdown}
                    onChange={(value) =>
                      handleInputChange(index, 'dropdown', value)
                    }
                  />
                  <br/>
                  <FormInput
                    type={InputType.DATE}
                    label={`${report.name} Date`}
                    selectedOption={report.date}
                    onChange={(date) =>
                      handleInputChange(index, 'date', date)
                    }
                  />
                </div>
              )}
            </div>
          ))}
          <button type="submit">Submit</button>
        </form>
      )}
    </div>
  )
}

export default AppNew
