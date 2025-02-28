import React, { ReactNode, useState } from "react"

type ShowSectionButtonProps = {
  triggerText: string
  children: ReactNode
}

const ShowConditionalContent: React.FC<ShowSectionButtonProps> = ({ triggerText, children }: ShowSectionButtonProps) => {
  const [showSection, setShowSection] = useState(false)

  const handleClick = () => {
    setShowSection(!showSection)
  }

  return (
    <>
      <button onClick={handleClick}>
        {triggerText}
      </button>
      {showSection && children}
    </>
  )
}

export default ShowConditionalContent
